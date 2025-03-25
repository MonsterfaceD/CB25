#!/usr/bin/php
<?php

GLOBAL $testNames;
$testNames = array(
	'kleeneVsConcat',
	'concatVsAlternative',
//	'setUnionVsSetIntersection',
//	'setIntersectionVsSetMinus',
//	'setUnionVsSetMinus',
	'simpleSetUnionVsSetIntersection',
	'simpleSetIntersectionVsSetMinus',
	'simpleSetUnionVsSetMinus',
	'vomBlatt',
	'standardRegex'
);

if (!isset($testFolder)) {
	$testFolder = 'autoTests';
}


function runTests($baseDir) {
	$totalTestCount = 0;
	$runResults = array(
		'success' => 0,
		'failure' => 0,
		'totalTestCount' => 0
	);

	GLOBAL $testNames;
	foreach ($testNames as $test) {
		$testData = file(getFilename($baseDir, $test));
		$entries = filterComments($testData);
		
		$entryCount = count($entries);
		if ($entryCount == 0) {
			echo "Internal Warning: Test '".$test."' has no tests! Skipping test.\r\n";
			continue;
		} else if ($entryCount % 3 != 0) {
			echo "Internal Warning: Test '".$test."' has mod 3 != 0 number of entries! Skipping test.\r\n";
			continue;
		}
		
		$tests = array();
		$testCount = $entryCount / 3;
		for ($i = 0; $i < $testCount; ++$i) {
			$solIsTrue = (strcasecmp($entries[3 * $i + 2], 'true') == 0);
			$solIsFalse = (strcasecmp($entries[3 * $i + 2], 'false') == 0);
			if ((!$solIsTrue) && (!$solIsFalse)) {
				echo "Internal Error: Solution '".$solution."' for regex '".$entries[3 * $i + 0]."' and input '".$entries[3 * $i + 1]."' is neither true nor false, can not parse! Skipping test.\r\n";
				continue;
			}
			$tests[] = array('regex' => str_replace(' ', '', $entries[3 * $i + 0]), 'input' => $entries[3 * $i + 1], 'solution' => $solIsTrue);
		}
		
		$runResult = runActualTests($tests);
		
		$totalTestCount += $testCount;
		$runResults['success'] += $runResult['success'];
		$runResults['failure'] += $runResult['failure'];
		$runResults['totalTestCount'] += $testCount;
	}

	echo "Ran ".$totalTestCount." tests, of which ".$runResults['failure']." failed (".$runResults['success']." succeeded).\r\n";
	return $runResults;
}

function runActualTests($tests) {
	$result = array(
		'success' => 0,
		'failure' => 0
	);

	$javaBaseCommand = 'java -cp ".'.PATH_SEPARATOR.'src'.PATH_SEPARATOR.'lib/antlr-4.7.1-complete.jar" RegexGrammarRunner ';
	
	$javaCommand = $javaBaseCommand;
	foreach ($tests as $pair) {
		$javaCommand .= '"'.$pair['regex'].'" "'.$pair['input'].'" ';
	}
	
	$output = array();
	exec($javaCommand.' 2>&1', $output);
		
	foreach ($tests as $pair) {
		$testResult = scanOutput($pair['regex'], $pair['input'], $pair['solution'], $output);
		if ($testResult) {
			$result['success']++;
		} else {
			$result['failure']++;
			echo "Failed test '".$pair['input']."' in '".$pair['regex']."'. Answer should be ".($pair['solution'] ? 'true' : 'false').", but it was not.\r\n";
		}
	}

	return $result;
}

function scanOutput($regex, $input, $correctSolution, $output) {
	$searchString = "'".$input."' is a member of '".$regex."'?";
	foreach ($output as $line) {
		if (strpos($line, 'Exception in thread') !== FALSE) {
			echo "Solution threw exception: ".str_replace("\n", '', str_replace("\r\n", '', implode(',', $output)))."\r\n";
			return FALSE;
		} else if (strpos($line, $searchString) !== FALSE) {
			$isMember = FALSE;
			if (endsWith($line, 'true')) {
				$isMember = TRUE;
			} else if (endsWith($line, 'false')) {
				$isMember = FALSE;
			} else {
				echo "Internal Error: Could not match output of runner: ".$line."\r\n";
				return FALSE;
			}
			
			return $isMember == $correctSolution;
		}
	}
	
	echo "Internal Error: Could not match output for regex '".$regex."' and input '".$input."'\r\n";
	echo "\t\tOutput was: ".implode(',', $output)."\r\n";
	return FALSE;
}

function endsWith($haystack, $needle) {
    $length = strlen($needle);
    if ($length == 0) {
        return true;
    }

    return (substr($haystack, -$length) === $needle);
}

function filterComments($lines) {
	$result = array();
	foreach ($lines as $line) {
		$line = trim($line);
		if ((strpos($line, '#') === 0) || (strpos($line, '//') === 0)) {
			continue;
		}
		$result[] = $line;
	}
	return $result;
}

function getFilename($dir, $test) {
	return $dir.'/'.$test.'.txt';
}

function hasSolution($dir, $test) {
	return file_exists(getFilename($dir, $test, TRUE));
}