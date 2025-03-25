#!/usr/bin/php
<?php

require_once('runTests.php');

GLOBAL $javaCommand;
GLOBAL $javaCCommand;
GLOBAL $antlrLibLocation;

// PATHES
//$javaCommand = "\"C:\\Program Files\\Java\\jre1.8.0_192\\bin\\java.exe\"";
$javaCommand = "java";
//$javaCCommand = "\"C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\javac.exe\"";
$javaCCommand = "javac";
//$antlrLibLocation = realpath("E:\\_Bibliothek\\CompilerBau\\CB18\\prog3\\RegexGrammar\\lib\\antlr-4.7.1-complete.jar");
$antlrLibLocation = realpath("/home/phoenix/Documents/teaching/WS20/cb/CB20/Exercises/prog3/RegexGrammarSolution/lib/antlr-4.7.1-complete.jar");

$bannedGroups = array(
	'S1605' => 'Shows GUI on every Regex, does not terminate on some inputs.',
	'S1259' => 'Shows GUI on every Regex, does not terminate on some inputs.',
);

function printHelp() {
	echo "Please call this script with the following arguments:\r\n";
	echo " - the zip file from L2P containing all solutions of the students and\r\n";
	echo " - the working folder where everything is extracted.\r\n";
	echo "Example: ./unzip.php studentSolutions.zip ./tmp\r\n";
}

if (count($argv) < 3) {
	printHelp();
	exit;
} else if (!file_exists($argv[1])) {
	echo "Error: The given file '".$argv[1]."' does not exist or is not readable.\r\n\r\n";
	printHelp();
	exit;
}
$workingDir = realpath($argv[2]);
if (!file_exists($workingDir) || ($workingDir == '')) {
	echo "Error: The given working directory '".$argv[2]."' does not exist or is not writable.\r\n\r\n";
	printHelp();
	exit;
}

echo "Working Dir = ".$workingDir."\r\n";

extractMoodleZip($argv[1], $workingDir, $bannedGroups);

function extractMoodleZip($absFileName, $workingDir, $bannedGroups) {
	$zip = new ZipArchive;
	$res = $zip->open($absFileName);
	if ($res === TRUE) {
		$count = $zip->count();
		$studentZips = array();
		for ($i = 0; $i < $count; ++$i) {
			$name = $zip->getNameIndex($i);
			echo "Name: ".$name."\r\n";
			if (preg_match('/([^_\/]+)_([^_\/]+)_([^\/]+)\/(.+).zip/', $name, $regs)) {
				# Successful match
				$exName = $regs[1];
				$groupId = $regs[2];
				$suffix = $regs[3];
				$file = $regs[4];

				
				
				$zip->extractTo($workingDir, $name);
				if (!rename($workingDir.'/'.$name, $workingDir.'/'.$groupId.'.zip')) {
					echo "Error: Could not rename ".$workingDir.'/'.$name." to ".$workingDir.'/'.$groupId.'.zip'."!\r\n";
					continue;
				}
				if (!rmdir($workingDir.'/'.$exName.'_'.$groupId.'_'.$suffix)) {
					die("Could not delete intermediate directory?!");
				}
				/*if (!rmdir($workingDir.'/'.$exNum)) {
					die("Could not delete intermediate directory?!");
				}*/
				$studentZips[] = $groupId;
			}
		}
		
		
		// extract it to the path we determined above
		//$zip->extractTo($workingDir);
		$zip->close();
		echo "Success! Extracted ".count($studentZips)." solutions from Moodle zip.\r\n";
		
		$points = array();
		foreach ($studentZips as $zip) {
			$result = checkStudentZip($workingDir, $zip, $bannedGroups);
			$points[$zip] = $result['points'];
		}
		
		echo "\r\n\r\n";
		echo "Overview:\r\n";
		foreach ($points as $group => $point) {
			echo $group.": ".$point."\r\n";
		}
	} else {
		echo "Error: Could not open Moodle zip '".$absFileName."'\r\n";
	}
}

function appendAndPrint($msg, $lines, $print = TRUE) {
	$lines[] = $msg;
	if ($print) {
		echo $msg."\r\n";
	}	
	return $lines;
}

function checkStudentZip($workingDir, $file, $bannedGroups) {
	// Safe CWD
	$currentCwd = getcwd();
	
	GLOBAL $javaCommand;
	GLOBAL $javaCCommand;
	GLOBAL $antlrLibLocation;
	
	$points = 0;
	$resultLines = array();
	
	
	$resultLines = appendAndPrint("------------------------------------------------------", $resultLines);
	$resultLines = appendAndPrint("Solution ".$file, $resultLines);
	$resultLines = appendAndPrint("", $resultLines);
	
	if (array_key_exists($file, $bannedGroups)) {
		$resultLines = appendAndPrint("ZERO points: group is banned (Reason: ".$bannedGroups[$file].")", $resultLines);
		return array('lines' => $resultLines, 'points' => $points);
	}

	$zip = new ZipArchive;
	$res = $zip->open($workingDir.'/'.$file.'.zip');
	if ($res === TRUE) {
		$count = $zip->count();
		$halfPoints = FALSE;
		$foundSrc = FALSE;
		$foundSrcItem = NULL;
		$basePathSrc = NULL;
		$foundGrammar = FALSE;
		$foundGrammarItem = NULL;
		$basePathGrammar = NULL;
		for ($i = 0; $i < $count; ++$i) {
			$name = $zip->getNameIndex($i); echo $name."\n";
			if (strpos($name, '__MACOSX') !== FALSE) {
				continue;
			} else if (strpos($name, '/production/') !== FALSE) {
				continue;
			} else if (strpos($name, '/classes/') !== FALSE) {
				continue;
			} else if (strpos($name, '/antlr4/') !== FALSE) {
				continue;
			}
			
			if (strpos($name, 'RegexGrammarRunner.java') !== FALSE) {
				if ($foundSrc) {
					$resultLines = appendAndPrint("Warning: Student solution ".$file." contains more than one RegexGrammarRunner.java!", $resultLines);
					$resultLines = appendAndPrint("\t\tLast path was ".$foundSrcItem.", current: ".$name, $resultLines);
				}
				$foundSrc = TRUE;
				$foundSrcItem = $name;
				if ($foundSrcItem != 'RegexGrammar/src/RegexGrammarRunner.java') {
					$halfPoints = TRUE;
				}
				$basePathSrc = str_replace('RegexGrammarRunner.java', '', $name);
			} else if (strpos($name, 'RegexGrammar.g4') !== FALSE) {
				if ($foundGrammar) {
					$resultLines = appendAndPrint("Warning: Student solution ".$file." contains more than one RegexGrammar.g4!", $resultLines);
					$resultLines = appendAndPrint("\t\tLast path was ".$foundGrammarItem.", current: ".$name, $resultLines);
				}
				$foundGrammar = TRUE;
				$foundGrammarItem = $name;
				if ($foundGrammarItem != 'RegexGrammar/RegexGrammar.g4') {
					$halfPoints = TRUE;
				}
				$basePathGrammar = str_replace('RegexGrammar.g4', '', $name);
			}
			
		}
		
		if ((!$foundSrc) || (!$foundGrammar)) {
			$resultLines = appendAndPrint("ZERO points: Can not check Student solution ".$file.", can not determine necessary paths! (does not include grammar and/or sources)", $resultLines);
			$points = 0;
		} else if ($basePathGrammar.'src/' != $basePathSrc) {
			$resultLines = appendAndPrint("ZERO points: Can not check Student solution ".$file.", location of grammar relative to sources does not match!", $resultLines);
			$resultLines = appendAndPrint("\t\tShould be '".$basePathGrammar.'src/'."' == '".$basePathSrc."'", $resultLines);
			$points = 0;
		} else {
			//$zip->extractTo($path);
			$basePath = $workingDir.'/x_'.$file;
			if (file_exists($basePath)) {
				rrmdir($basePath);
			}
			if (!mkdir($basePath)) {
				die("Could not create intermediate directory?!");
			}
			$basePath = realpath($basePath);
			$zip->extractTo($basePath);
			
			// Delete class files, if any
			recursiveDeleteClassFiles($basePath);
			
			// Compile grammar
			if (!chdir($basePath.'/'.$basePathGrammar)) {
				die("Could not chdir to grammar: ".$basePath.'/'.$basePathGrammar);
			}
			
			$output = array();
			$returnVar = -1;
			$cmd = $javaCommand.' -jar '.$antlrLibLocation.' -visitor RegexGrammar.g4 2>&1';
			exec($cmd, $output, $returnVar);
			
			if ($returnVar != 0) {
				$resultLines = appendAndPrint("ZERO points: Failed to translate RegexGrammar.g4.", $resultLines);
				$resultLines = appendAndPrint("\t\tCommand: ".$cmd, $resultLines);
				$resultLines = appendAndPrint("\t\tOutput: ".implode(',', $output), $resultLines);
				$points = 0;
			} else {
				// Compile sources
				// -cp ".;src;lib/antlr-4.7.1-complete.jar" .\src\*.java
				$output = array();
				$returnVar = -1;
				$cmd = $javaCCommand.' -cp ".'.PATH_SEPARATOR.'src'.PATH_SEPARATOR.$antlrLibLocation.'" ./src/*.java 2>&1';
				exec($cmd, $output, $returnVar);
				if ($returnVar != 0) {
					$resultLines = appendAndPrint("ZERO points: Failed to compile Java code.", $resultLines);
					$resultLines = appendAndPrint("\t\tCommand: ".$cmd, $resultLines);
					$resultLines = appendAndPrint("\t\tOutput: ".implode(',', $output), $resultLines);
					$points = 0;
				} else {
					$basePathTests = realpath($currentCwd.'/autoTests');
					$runResults = runTests($basePathTests);
					
					$points = round(($halfPoints ? 0.5 : 1) * 20.0 * (((double)$runResults['success']) / ((double)$runResults['totalTestCount'])), 1);
				}
			}
			
			if ($halfPoints) {
				$resultLines = appendAndPrint("HALF points: Student solution ".$file." has incorrect path (theirs: ".$foundSrcItem.", should be 'RegexGrammar/src/RegexGrammarRunner.java', theirs: ".$foundGrammarItem.", should be 'RegexGrammar/RegexGrammar.g4').", $resultLines);
			}
			$resultLines = appendAndPrint("-- TOTAL POINTS: ".$points, $resultLines);
			
			// Change directory before deleting
			if (!chdir($currentCwd)) {
				die("Could not chdir back out");
			}
			
			// Delete everything in the end
			rrmdir($basePath);
		}
		$zip->close();
	} else {
		$resultLines = appendAndPrint("Error: Could not open student zip '".$file."'", $resultLines);
	}
	
	// Restore CWD
	chdir($currentCwd);
	
	return array('lines' => $resultLines, 'points' => $points);
}

function recursiveDeleteClassFiles($dir) {
	if (is_dir($dir)) { 
		$objects = scandir($dir); 
		foreach ($objects as $object) { 
			if ($object != '.' && $object != '..') {
				$name = realpath($dir.'/'.$object);
				if (is_dir($name)) {
					recursiveDeleteClassFiles($name);
				} else {
					if (endsWith($object, '.class')) {
						unlink($name);
					}
				}
			}
		}
	}
}

function rrmdir($dir) { 
	if (is_dir($dir)) { 
		$objects = scandir($dir); 
		foreach ($objects as $object) { 
			if ($object != '.' && $object != '..') {
				$name = realpath($dir.'/'.$object);
				if (is_dir($name)) {
					rrmdir($name);
				} else {
					unlink($name);
				}
			}
		}
		@rmdir($dir); 
	}
}
