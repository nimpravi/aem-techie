/*
 * Copyright 1997-2009 Day Management AG
 * Barfuesserplatz 6, 4001 Basel, Switzerland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Day Management AG, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Day.
 */

if( CQ_Analytics.OperatorActions ) {
    //set i18n translations

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.EQUALS, CQ.I18n.getMessage("equals"));
    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.IS, CQ.I18n.getMessage("is"));

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.NOT_EQUAL, CQ.I18n.getMessage("is not equal to"));

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.GREATER, CQ.I18n.getMessage("is greater than"));
    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.GREATER_OR_EQUAL, CQ.I18n.getMessage("is equal to or greater than"));

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.OLDER, CQ.I18n.getMessage("is older than"));
    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.OLDER_OR_EQUAL, CQ.I18n.getMessage("is equal to or older than"));

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.LESS, CQ.I18n.getMessage("is less than"));
    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.LESS_OR_EQUAL, CQ.I18n.getMessage("is equal to or less than"));

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.YOUNGER, CQ.I18n.getMessage("is younger than"));
    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.YOUNGER_OR_EQUAL, CQ.I18n.getMessage("is equal to or younger than"));

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.CONTAINS, CQ.I18n.getMessage("contains", null, "Ex: language contains french, Ex: gender contains female"));

    CQ_Analytics.OperatorActions.setText(CQ_Analytics.Operator.BEGINS_WITH, CQ.I18n.getMessage("begins with", null, "Ex: title begins with News"));
}