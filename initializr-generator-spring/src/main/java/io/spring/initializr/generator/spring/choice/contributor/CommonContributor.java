package io.spring.initializr.generator.spring.choice.contributor;

import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.text.MustacheSection;
import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lanboo
 * @date 2020/5/6 17:48
 */
public class CommonContributor implements ProjectContributor {
    private ProjectDescription description;
    private MustacheTemplateRenderer mustache;

    public CommonContributor(ProjectDescription projectDescription, MustacheTemplateRenderer mustacheTemplateRenderer) {
        this.description = projectDescription;
        this.mustache = mustacheTemplateRenderer;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        SourceStructure sourceStructure = this.description.getBuildSystem().getMainSource(projectRoot, this.description.getLanguage());

        final String PACKAGE_NAME = "packageName";
        final String PARENT_PACKAGE_NAME = "parentPackageName";
        final String packageName = description.getPackageName();

        // 创建advice
        final String advicePackageName = packageName + ".advice";
        Path globalExceptionAdvicePath = sourceStructure.createSourceFile(advicePackageName, "GlobalExceptionAdvice");
        Files.createDirectories(globalExceptionAdvicePath.getParent());
        Map<String, Object> adviceMap = new LinkedHashMap<>();
        adviceMap.put(PACKAGE_NAME, advicePackageName);
        adviceMap.put(PARENT_PACKAGE_NAME, packageName);
        MustacheSection globalExceptionMustache = new MustacheSection(mustache, "choice/GlobalExceptionAdvice", adviceMap);
        try(PrintWriter adviceWriter = new PrintWriter(globalExceptionAdvicePath.toFile())) {
            globalExceptionMustache.write(adviceWriter);
        }

        // ServiceException
        final String exceptionPackageName = packageName + ".exception";
        Path serviceExceptionPath = sourceStructure.createSourceFile(exceptionPackageName, "ServiceException");
        Files.createDirectories(serviceExceptionPath.getParent());
        Map<String, Object> exceptionMap = new LinkedHashMap<>();
        exceptionMap.put(PACKAGE_NAME, exceptionPackageName);
        exceptionMap.put(PARENT_PACKAGE_NAME, packageName);
        MustacheSection exceptionMustache = new MustacheSection(mustache, "choice/ServiceException", exceptionMap);
        try(PrintWriter exceptionWriter = new PrintWriter(serviceExceptionPath.toFile())) {
            exceptionMustache.write(exceptionWriter);
        }

        // ResponseData
        final String responsePackageName = packageName + "response";
        Path responsePath = sourceStructure.createSourceFile(responsePackageName, "ResponseData");
        Files.createDirectories(responsePath.getParent());
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put(PACKAGE_NAME, responsePackageName);
        MustacheSection responseMustache = new MustacheSection(mustache, "choice/ResponseData", responseMap);
        try(PrintWriter responseWriter = new PrintWriter(responsePath.toFile())) {
            responseMustache.write(responseWriter);
        }

        // ResponseCode
        final String responseCodePackageName = responsePackageName + "code";
        Path responseCodePath = sourceStructure.createSourceFile(responseCodePackageName, "ResponseInfo");
        Files.createDirectories(responseCodePath.getParent());
        Map<String, Object> responseCodeMap = new LinkedHashMap<>();
        responseCodeMap.put(PACKAGE_NAME, responseCodePackageName);
        responseCodeMap.put(PARENT_PACKAGE_NAME, packageName);
        MustacheSection responseCodeMustache = new MustacheSection(mustache, "choice/ResponseInfo", responseCodeMap);
        try(PrintWriter responseCodeWriter = new PrintWriter(responseCodePath.toFile())) {
            responseCodeMustache.write(responseCodeWriter);
        }

        // SysResponseCode
        Path sysResponseCode = sourceStructure.createSourceFile(responseCodePackageName, "SysResponseCode");
        MustacheSection sysResponseCodeMustache = new MustacheSection(mustache, "choice/SysResponseCode", responseCodeMap);
        try(PrintWriter sysResponseCodeWriter = new PrintWriter(sysResponseCode.toFile())) {
            sysResponseCodeMustache.write(sysResponseCodeWriter);
        }
    }
}
