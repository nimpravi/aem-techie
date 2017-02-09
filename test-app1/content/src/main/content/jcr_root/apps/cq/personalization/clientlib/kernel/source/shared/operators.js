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
/**
 * The <code>CQ_Analytics.Operator</code> object is a singleton providing the most common operator names and
 * utils around the operators.
 * @singleton
 * @class CQ_Analytics.Operator
 */
CQ_Analytics.Operator = (function() {
    return function () {
    };
})();

/**
 * Operator "is".
 * @static
 * @type String
 */
CQ_Analytics.Operator.IS = "is";

/**
 * Operator "equals".
 * @static
 * @type String
 */
CQ_Analytics.Operator.EQUALS = "equals";

/**
 * Operator "not equals".
 * @static
 * @type String
 */
CQ_Analytics.Operator.NOT_EQUAL = "notequal";

/**
 * Operator "greater than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.GREATER = "greater";

/**
 * Operator "equals or greater than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.GREATER_OR_EQUAL = "greaterorequal";

/**
 * Operator "older than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.OLDER = "older";

/**
 * Operator "equals or older than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.OLDER_OR_EQUAL = "olderorequal";

/**
 * Operator "less than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.LESS = "less";

/**
 * Operator "equals or less than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.LESS_OR_EQUAL = "lessorequal";

/**
 * Operator "younger than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.YOUNGER = "younger";

/**
 * Operator "equals or younger than".
 * @static
 * @type String
 */
CQ_Analytics.Operator.YOUNGER_OR_EQUAL = "youngerorequal";

/**
 * Operator "contains".
 * @static
 * @type String
 */
CQ_Analytics.Operator.CONTAINS = "contains";

/**
 * Operator "begins with".
 * @static
 * @type String
 */
CQ_Analytics.Operator.BEGINS_WITH = "beginswith";

/**
 * The <code>CQ_Analytics.OperatorActions</code> object is a singleton providing utilities to resolve operations
 * containing operators (type of <code>CQ_Analytics.Operators</code>).
 */
CQ_Analytics.OperatorActions = function() {
    var mapping = {};

    var addOperator = function(name, text, operation) {
        mapping[name] = [text, operation];
    };

    addOperator(CQ_Analytics.Operator.EQUALS, "equals", "==");
    addOperator(CQ_Analytics.Operator.IS, "is", "==");

    addOperator(CQ_Analytics.Operator.NOT_EQUAL, "is not equal to", "!=");

    addOperator(CQ_Analytics.Operator.GREATER, "is greater than", ">");
    addOperator(CQ_Analytics.Operator.GREATER_OR_EQUAL, "is equal to or greater than", ">=");

    addOperator(CQ_Analytics.Operator.OLDER, "is older than", ">");
    addOperator(CQ_Analytics.Operator.OLDER_OR_EQUAL, "is equal to or older than", ">=");

    addOperator(CQ_Analytics.Operator.LESS, "is less than", "<");
    addOperator(CQ_Analytics.Operator.LESS_OR_EQUAL, "is equal to or less than", "<=");

    addOperator(CQ_Analytics.Operator.YOUNGER, "is younger than", "<");
    addOperator(CQ_Analytics.Operator.YOUNGER_OR_EQUAL, "is equal to or younger than", "<=");

    addOperator(CQ_Analytics.Operator.CONTAINS, "contains", function(s1, s2) {
        if (s1) {
            if (s2) {
                s1 = "" + s1;
                s2 = "" + s2;
                return s1.toLowerCase().indexOf(s2.toLowerCase()) != -1;
            }
            return true;
        }
        return false;
    });

    addOperator(CQ_Analytics.Operator.BEGINS_WITH, "begins with", function(s1, s2) {
        if (s1) {
            if (s2) {
                s1 = "" + s1;
                s2 = "" + s2;
                return s1.toLowerCase().indexOf(s2.toLowerCase()) == 0;
            }
            return true;
        }
        return false;
    });

    var getByIndex = function(op, index) {
        if (mapping[op] && mapping[op][index]) {
            return mapping[op][index];
        }
        return "";
    };

    var escapeQuote = function(str) {
        if (str) {
            str = str.replace(new RegExp("\\'", "ig"), str);
        }
        return str;
    };

    return {
        /**
         * Returns operator friendly name.
         * @param {CQ_Analytics.Operator} operator
         * @return {String} text if defined, empty string otherwise.
         */
        getText: function(operator) {
            return getByIndex(operator, 0);
        },

        /**
         * Set operator friendly name.
         * @param {CQ_Analytics.Operator} operator
         * @param {String} newText The next text
         */
        setText: function(operator, newText) {
            if (mapping[operator] && mapping[operator][0]) {
                mapping[operator][0] = newText;
            }
        },

        /**
         * Returns operator operation, which can be either:<ul>
         * <li>String: mathematical JS operator like ==, <, <=, > ... </li>
         * <li>Function: function requiring 2 parameters, the 2 values to operate and which returns true if operation
         * success, false otherwise.
         * Example: contains operator function.<code>
         function(s1, s2) {
        if (s1) {
            if (s2) {
                s1 = "" + s1;
                s2 = "" + s2;
                return s1.toLowerCase().indexOf(s2.toLowerCase()) != -1;
            }
            return true;
        }
        return false;
    }
         * </code></li>
         * </ul>
         * @param {CQ_Analytics.Operator} operator
         * @return {String/Function} Operator string or function if operator is defined, empty string otherwise.
         */
        getOperation: function(operator) {
            return getByIndex(operator, 1);
        },

        /**
         * Operates a property value and a value with an operator. Sample: <code>
           var obj = {};
           obj["age"] = 30;
           CQ_Analytics.OperatorActions.operate(obj, "age", CQ_Analytics.Operator.IS, "30", "parseInt"); //returns true
           CQ_Analytics.OperatorActions.operate(obj, "age", CQ_Analytics.Operator.GREATER_THAN, "40", "parseInt"); //returns false
         * </code>
         *
         * @param {Object} object Value container.
         * @param {String} property Name of the propert to operate.
         * @param {CQ_Analytics.Operator} operator
         * @param {String} value The second value of the operation
         * @param {String} valueFormat (optional) An optional value formatter (parseInt, parseFloat, toString...)
         * @return {Boolean} true if operation success, false otherwise.
         */
        operate: function(object, property, operator, value, valueFormat) {
            try {
                if (object && object[property]) {
                    var toEval = "";
                    var op = this.getOperation(operator);
                    op = op ? op : operator;
                    var objectValue = CQ.shared.XSS.getXSSTablePropertyValue(object, property);
                    if (typeof op == "function") {
                        return op.call(this, objectValue, value, valueFormat);
                    } else {
                        if (valueFormat) {
                            toEval = valueFormat + "('" + objectValue + "') " + op + " " + valueFormat + "('" + value + "')";
                        } else {
                            var s1 = escapeQuote(objectValue);
                            var s2 = escapeQuote(value);
                            toEval = "'" + s1 + "' " + op + " '" + s2 + "'";
                        }
                        var b = eval(toEval);
                        return b;
                    }

                }
            } catch(e) {
                //console.log("Error in Operator resolution", e, toEval);
            }
            return false;
        }
    };
}();