package org.strangeforest.tcb.stats.controller;

import java.util.*;

import org.springframework.boot.web.servlet.error.*;
import org.springframework.web.context.request.*;
import org.thymeleaf.exceptions.*;

import static com.google.common.base.Throwables.*;
import static com.google.common.collect.Lists.*;

public class TennisStatsErrorAttributes extends DefaultErrorAttributes {

	@Override public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
		errorAttributes.put("inFragment", isInFragment(webRequest));
		errorAttributes.put("versions", BaseController.VERSIONS);
		return errorAttributes;
	}

	private boolean isInFragment(WebRequest webRequest) {
		Throwable error = getError(webRequest);
		if (error == null)
			return false;
		return reverse(getCausalChain(error)).stream()
			.filter(th -> th instanceof TemplateProcessingException)
			.map(th -> (TemplateProcessingException)th)
			.map(tEx -> tEx.hasTemplateName() && tEx.getTemplateName().startsWith("fragments/"))
			.findFirst().orElse(false);
	}
}
