'''
Created on Nov 6, 2014

This scripts checks the cs-studio features to verify if there is 
mismatch between the feature version number and the version number of the plugins included in it.

@author: Kunal Shroff
'''
import os.path
import xml.etree.ElementTree as ET
from xml.dom import minidom
from optparse import OptionParser
import re
import sys


def readFeatures(repoDir):
    '''
    Read the all feature.xml
    {id:
        {id:'feature.id',
        version: 'feature.version'
        file:'complete file path'
        plugins:['plugin.ids']
        includes:['included.features']
        }
    }
    '''
    features = {}
    for dirpath, dirnames, filenames in os.walk(os.path.normpath(repoDir)):
        for completefilename in [ os.path.join(dirpath, f) for f in filenames if f.endswith("feature.xml")]:
            xmldoc = minidom.parse(completefilename)
            for feature in xmldoc.getElementsByTagName('feature'):
                id = feature._attrs[u'id'].value
                version = feature._attrs[u'version'].value
                plugins = []
                for node in feature.getElementsByTagName('plugin'):
                    plugins.append(node._attrs[u'id'].value)
                includes = []
                for node in feature.getElementsByTagName('includes'):
                    includes.append(node._attrs[u'id'].value)
            features[id] = {'id':id, 'version':version, 'file':completefilename, 'plugins':plugins, 'includes':includes}
    return features

def readPluginManifests(repoDir):
    '''
    Read the manifest file create the dependency tree
    It will get all the Manifest files in the repoDir and create tree
    of the dependencies of each plugins.
      
    {id: {name:'',
         version:'',
         file:'complete file path',
         dependencies:[,,]
         }
    }
    '''
    plugins = {}
    for dirpath, dirnames, filenames in os.walk(os.path.normpath(repoDir)):
        for completefilename in [ os.path.join(dirpath, f) for f in filenames if f.endswith(".MF")]:
            name = ''
            dependencies = []
            fileString = open(completefilename, 'r').read().replace(',\n', ',')
            for line in fileString.split('\n'):
                m = re.search('Bundle-SymbolicName: (.*)', line)
                if m:
                    name = m.group(1).split(';')[0].strip()
                m1 = re.search('Bundle-Version: (.*)', line)
                if m1:
                    version = m1.group(1).strip()                
                m2 = re.match('Require-Bundle: (.*)', line)
                if m2:
                    for dependency in m2.group(1).split(','):                        
                        dependencyDetails = dependency.split(';')
                        details = {}
                        for detail in dependencyDetails:
                            matchVersion = re.match('bundle-version="(.*)"', detail.strip())
                            if matchVersion:
                                details['version'] = matchVersion.group(1)
                            matchResolution = re.match('resolution:=(.*)', detail.strip())
                            if matchResolution:
                                details['resolution'] = matchResolution.group(1)
                        '''
                        Ignore optional dependencies, if resolution is not specified it is assumed to be required
                        '''
                        if 'resolution' not in details.keys() or details['resolution'] != 'optional':
                            dependencies.append(dependency.split(';')[0].strip())                                                                                                                  
            plugins[name] = {'name':name, 'version':version, 'file':completefilename, 'dependencies':dependencies}
    return plugins

def ifNoneReturnDefault(object, default):
    '''
    if the object is None or empty string then this function returns the default value
    '''
    if object == None and object != '':
        return default
    else:
        return object    
    
if __name__ == '__main__':
    repoDir = 'C:\git\cs-studio-organization-master\cs-studio'
    
    usage = 'usage: %prog -r C:\git\cs-studio'
    parser = OptionParser(usage=usage)
    parser.add_option('-r', '--repoDir', \
                      action='store', type='string', dest='repoDir', \
                      help='the repoDir')
    opts, args = parser.parse_args()
    repoDir = ifNoneReturnDefault(opts.repoDir, repoDir)
    
    '''
    since ElementTree does not allow easy access to the name space, simply setting it as a variable
    '''
    xmlns="{http://maven.apache.org/POM/4.0.0}"
    
    issues = []
    
    '''all the features in the core repository'''
    allFeatures = readFeatures(repoDir)
    allplugins = readPluginManifests(repoDir)
    
    coreDir = os.path.join(repoDir, 'core')
    appDir = os.path.join(repoDir, 'applications')
    
    '''
    Core Validation:
    read the features/pom.xml for the list of core features
    '''
    root = ET.parse(os.path.join(coreDir, 'features', 'pom.xml')).getroot()
    
    '''the features defined in the pom'''
    coreFeatures = []
    for module in root.iter(xmlns + 'module'):
        featureName = module.text        
        coreFeatures.append(module.text)
    
    for coreFeature in coreFeatures:
        versionMismatch = []
        featureVersion = allFeatures[coreFeature]['version']
        for includedPlugin in allFeatures[coreFeature]['plugins']:
            if includedPlugin not in allplugins:
                versionMismatch.append(includedPlugin+':unknown')
            elif featureVersion != allplugins[includedPlugin]['version']:
                versionMismatch.append(includedPlugin+':'+allplugins[includedPlugin]['version'])
        issues.append(coreFeature+':'+featureVersion+' has included plugins with version mismatch '+';'.join(versionMismatch))
    
    '''
    Applications Validation:
    read the features/pom.xml for the list of application features
    '''
    root = ET.parse(os.path.join(appDir, 'features', 'pom.xml')).getroot()
    '''all the features in the applications repository'''
    allAppFeatures = readFeatures(os.path.join(appDir, 'features'))
    '''the features defined in the pom'''
    appFeatures = []
    for module in root.iter(xmlns + 'module'):
        featureName = module.text        
        appFeatures.append(module.text)
        
    for appFeature in appFeatures:
        versionMismatch = []
        featureVersion = allFeatures[appFeature]['version']
        for includedPlugin in allFeatures[appFeature]['plugins']:
            if includedPlugin not in allplugins:
                versionMismatch.append(includedPlugin+':unknown')
            elif featureVersion != allplugins[includedPlugin]['version']:
                versionMismatch.append(includedPlugin+':'+allplugins[includedPlugin]['version'])
        issues.append(appFeature+':'+featureVersion+' has included plugins with version mismatch '+';'.join(versionMismatch))       
    
    for issue in issues:
        print issue           

    if len(issues) != 0:
        sys.exit(-1)
    
