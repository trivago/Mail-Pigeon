package com.trivago.mail.pigeon.web;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.ui.Window;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.IOException;

public class JavaScriptInjectorServlet extends ApplicationServlet
{

    @Override
    protected void writeAjaxPageHtmlVaadinScripts(Window window,
            String themeName, Application application, BufferedWriter page,
            String appUrl, String themeUri, String appId,
            HttpServletRequest request) throws ServletException, IOException {
        page.write("<script type=\"text/javascript\">\n");
        page.write("//<![CDATA[\n");
        page.write(new StringBuilder().append("document.write(\"<script language='javascript' src='").append(request.getContextPath()).append("/jquery/jquery-1.4.4.min.js'><\\/script>\");\n").toString());
        page.write(new StringBuilder().append("document.write(\"<script language='javascript' src='").append(request.getContextPath()).append("/js/highcharts.js'><\\/script>\");\n").toString());
        page.write(new StringBuilder().append("document.write(\"<script language='javascript' src='").append(request.getContextPath()).append("/js/modules/exporting.js'><\\/script>\");\n").toString());
        page.write("//]]>\n</script>\n");
        super.writeAjaxPageHtmlVaadinScripts(window, themeName, application,
                page, appUrl, themeUri, appId, request);
    }

}
