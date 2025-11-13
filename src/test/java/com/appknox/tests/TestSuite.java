package com.appknox.tests;

import com.appknox.tests.authentication.AuthenticationTests;
import com.appknox.tests.upload.UploadTests;
import com.appknox.tests.information.InformationTests;
import com.appknox.tests.cicheck.CICheckTests;
import com.appknox.tests.reports.ReportTests;
import com.appknox.tests.analyses.AnalysesTests;
import com.appknox.tests.cliflags.CLIFlagsTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Appknox CLI Complete Test Suite")
@SelectClasses({
    AuthenticationTests.class,// 1st - Run authentication first
    InformationTests.class,   //2nd - Information tests  
    UploadTests.class,            //3rd - Upload tests
    AnalysesTests.class,      //4th - Analyses tests
    CICheckTests.class,         //5th - CI Check tests                  
    ReportTests.class,      //6th - Report tests                            
    CLIFlagsTests.class     //7th - CLI Flags tests            
})
public class TestSuite {
    
}