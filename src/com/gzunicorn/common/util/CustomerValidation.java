/*
 * Created on 2005-8-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.util;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CustomerValidation implements Serializable {

	public static boolean validateTwoFields(Object bean, ValidatorAction va,
			Field field, ActionErrors errors, HttpServletRequest request) {
		String value = ValidatorUtil
				.getValueAsString(bean, field.getProperty());
		String sProperty2 = field.getVarValue("secondProperty");
		String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);

		if (!GenericValidator.isBlankOrNull(value)) {
			try {
				if (!value.equals(value2)) {
					errors.add(field.getKey(), Resources.getActionError(
							request, va, field));

					return false;
				}
			} catch (Exception e) {
				errors.add(field.getKey(), Resources.getActionError(request,
						va, field));

				return false;
			}
		}

		return true;
	}
	
	public static boolean validateTwoFields2(Object bean, ValidatorAction va,
			Field field, ActionErrors errors, HttpServletRequest request) {
		String value = ValidatorUtil
				.getValueAsString(bean, field.getProperty());
		String sProperty2 = field.getVarValue("secondProperty");
		String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);

		if (!GenericValidator.isBlankOrNull(value)) {
			try {
				if (value.equals(value2)) {
					errors.add(field.getKey(), Resources.getActionError(
							request, va, field));

					return false;
				}
			} catch (Exception e) {
				errors.add(field.getKey(), Resources.getActionError(request,
						va, field));

				return false;
			}
		}

		return true;
	}

}
