package org.jwat.tools.tasks.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jwat.common.UriProfile;
import org.jwat.tools.ExitException;
import org.jwat.tools.NoExitSecurityManager;
import org.jwat.tools.validators.XmlValidatorPlugin;

import com.antiaction.common.cli.CommandLine;

@RunWith(JUnit4.class)
public class TestTestTaskCLIParser {

	private SecurityManager securityManager;

	@Before
	public void setUp() {
		securityManager = System.getSecurityManager();
		System.setSecurityManager(new NoExitSecurityManager());
	}

	@After
	public void tearDown() {
		System.setSecurityManager(securityManager);
	}

	@Test
	public void test_testtask_cli_parser() {
		CommandLine cmdLine;
		TestOptions options;

		TestTaskCLIParser object = new TestTaskCLIParser();
		Assert.assertNotNull(object);

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setLenient(false);

		Object[][] cases = new Object[][] {
			{
				new String[] {"-w", "8", "file1"},
				8,
				false, true, false, 0L, UriProfile.RFC3986, false,
				new String[] {"file1"}
			},
			{
				new String[] {"-e", "file2"},
				1,
				true, true, false, 0L, UriProfile.RFC3986, false,
				new String[] {"file2"}
			},
			{
				new String[] {"-i", "file3"},
				1,
				false, false, false, 0L, UriProfile.RFC3986, false,
				new String[] {"file3"}
			},
			/*
			{
				new String[] {"--ignore-digest", "file4"},
				1,
				false, false, false, 0L, UriProfile.RFC3986, false,
				new String[] {"file4"}
			},
			*/
			{
				new String[] {"-b", "file5"},
				1,
				false, true, true, 0L, UriProfile.RFC3986, false,
				new String[] {"file5"}
			},
			{
				new String[] {"-a", dateFormat.format(date), "file6"},
				1,
				false, true, false, (date.getTime() / 1000) * 1000, UriProfile.RFC3986, false,
				new String[] {"file6"}
			},
			{
				new String[] {"-l", "file7"},
				1,
				false, true, false, 0L, UriProfile.RFC3986_ABS_16BIT_LAX, false,
				new String[] {"file7"}
			},
			{
				new String[] {"-x", "file8"},
				1,
				false, true, false, 0L, UriProfile.RFC3986, true,
				new String[] {"file8"}
			},
		};

		for (int i=0; i<cases.length; ++i) {
			cmdLine = new CommandLine();
			cmdLine.argsArray = (String[])cases[ i ][ 0 ];
			options = TestTaskCLIParser.parseArguments(cmdLine);
			Assert.assertEquals(cases[ i ][ 1 ], options.threads);
			Assert.assertEquals(cases[ i ][ 2 ], options.bShowErrors);
			Assert.assertEquals(cases[ i ][ 3 ], options.bValidateDigest);
			Assert.assertEquals(cases[ i ][ 4 ], options.bBad);
			Assert.assertEquals(cases[ i ][ 5 ], options.after);
			Assert.assertEquals(cases[ i ][ 6 ], options.uriProfile);
			if ((Boolean)cases[ i ][ 7 ]) {
				Assert.assertEquals(1, options.validatorPlugins.size());
				Assert.assertTrue(options.validatorPlugins.get( 0 ) instanceof XmlValidatorPlugin);
			}
			else {
				Assert.assertEquals(0, options.validatorPlugins.size());
			}
			String[] expectedFileList = (String[])cases[ i ][ 8 ];
			List<String> fileList = options.filesList;
			Assert.assertEquals(expectedFileList.length, fileList.size());
			for (int j=0; j<expectedFileList.length; ++j) {
				Assert.assertEquals(expectedFileList[ j ], fileList.get( j ));
			}
		}

		try {
			cmdLine = new CommandLine();
			cmdLine.argsArray = new String[] {"-w", "-1", "file"};
			options = TestTaskCLIParser.parseArguments(cmdLine);
			Assert.fail("Exception expected!");
		}
		catch (ExitException e) {
		}

		try {
			cmdLine = new CommandLine();
			cmdLine.argsArray = new String[] {"-w", "42"};
			options = TestTaskCLIParser.parseArguments(cmdLine);
			Assert.fail("Exception expected!");
		}
		catch (ExitException e) {
		}

		try {
			cmdLine = new CommandLine();
			cmdLine.argsArray = new String[] {"-w", "fourtytwo"};
			options = TestTaskCLIParser.parseArguments(cmdLine);
			Assert.fail("Exception expected!");
		}
		catch (ExitException e) {
		}

		try {
			cmdLine = new CommandLine();
			cmdLine.argsArray = new String[] {"-a", "fourtytwo"};
			options = TestTaskCLIParser.parseArguments(cmdLine);
			Assert.fail("Exception expected!");
		}
		catch (ExitException e) {
		}
	}

}
