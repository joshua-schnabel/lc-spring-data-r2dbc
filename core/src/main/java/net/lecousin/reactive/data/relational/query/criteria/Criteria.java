package net.lecousin.reactive.data.relational.query.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.lecousin.reactive.data.relational.schema.dialect.RelationalDatabaseSchemaDialect.SqlFunction;

public interface Criteria {
	
	<T> T accept(CriteriaVisitor<T> visitor);
	
	default Criteria and(Criteria criteria) {
		return new And(this, criteria);
	}
	
	default Criteria or(Criteria criteria) {
		return new Or(this, criteria);
	}
	
	static PropertyOperand property(String entityName, String propertyName) {
		return new PropertyOperand(entityName, propertyName);
	}

	public static class And implements Criteria {
		private Criteria left;
		private Criteria right;
		
		public And(Criteria left, Criteria right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public <T> T accept(CriteriaVisitor<T> visitor) {
			return visitor.visit(this);
		}
		
		public Criteria getLeft() {
			return left;
		}

		public Criteria getRight() {
			return right;
		}
		
		@Override
		public String toString() {
			return "(" + left.toString() + " AND " + right.toString() + ")";
		}
	}

	public static class Or implements Criteria {
		private Criteria left;
		private Criteria right;
		
		public Or(Criteria left, Criteria right) {
			this.left = left;
			this.right = right;
		}
		
		public Criteria getLeft() {
			return left;
		}

		public Criteria getRight() {
			return right;
		}

		@Override
		public <T> T accept(CriteriaVisitor<T> visitor) {
			return visitor.visit(this);
		}
		
		@Override
		public String toString() {
			return "(" + left.toString() + " OR " + right.toString() + ")";
		}
	}
	
	public enum PropertyOperator {
		EQUALS,
		NOT_EQUALS,
		GREATER_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN,
		LESS_THAN_OR_EQUAL,
		IS_NULL,
		IS_NOT_NULL,
		LIKE,
		NOT_LIKE,
		IN,
		NOT_IN;
	}
	
	public static class PropertyOperand {
		private String entityName;
		private String propertyName;
		private LinkedList<SqlFunction> functionsToApply = new LinkedList<>();
		
		public PropertyOperand(String entityName, String propertyName) {
			this.entityName = entityName;
			this.propertyName = propertyName;
		}

		public String getEntityName() {
			return entityName;
		}

		public String getPropertyName() {
			return propertyName;
		}
		
		public List<SqlFunction> getFunctionsToApply() {
			return new ArrayList<>(functionsToApply);
		}
		
		public Criteria is(Object value) {
			return new PropertyOperation(this, PropertyOperator.EQUALS, value);
		}
		
		public Criteria is(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.EQUALS, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria isNot(Object value) {
			return new PropertyOperation(this, PropertyOperator.NOT_EQUALS, value);
		}
		
		public Criteria isNot(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.NOT_EQUALS, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria greaterThan(Object value) {
			return new PropertyOperation(this, PropertyOperator.GREATER_THAN, value);
		}
		
		public Criteria greaterThan(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.GREATER_THAN, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria greaterOrEqualTo(Object value) {
			return new PropertyOperation(this, PropertyOperator.GREATER_THAN_OR_EQUAL, value);
		}
		
		public Criteria greaterOrEqualTo(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.GREATER_THAN_OR_EQUAL, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria lessThan(Object value) {
			return new PropertyOperation(this, PropertyOperator.LESS_THAN, value);
		}
		
		public Criteria lessThan(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.LESS_THAN, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria lessOrEqualTo(Object value) {
			return new PropertyOperation(this, PropertyOperator.LESS_THAN_OR_EQUAL, value);
		}
		
		public Criteria lessOrEqualTo(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.LESS_THAN_OR_EQUAL, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria isNull() {
			return new PropertyOperation(this, PropertyOperator.IS_NULL, null);
		}
		
		public Criteria isNotNull() {
			return new PropertyOperation(this, PropertyOperator.IS_NOT_NULL, null);
		}
		
		public Criteria like(Object value) {
			return new PropertyOperation(this, PropertyOperator.LIKE, value);
		}
		
		public Criteria like(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.LIKE, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria notLike(Object value) {
			return new PropertyOperation(this, PropertyOperator.NOT_LIKE, value);
		}
		
		public Criteria notLike(String entityName, String propertyName) {
			return new PropertyOperation(this, PropertyOperator.NOT_LIKE, new PropertyOperand(entityName, propertyName));
		}
		
		public Criteria in(Collection<?> values) {
			return new PropertyOperation(this, PropertyOperator.IN, values);
		}
		
		public Criteria notIn(Collection<?> values) {
			return new PropertyOperation(this, PropertyOperator.NOT_IN, values);
		}
		
		public PropertyOperand toUpperCase() {
			functionsToApply.addFirst(SqlFunction.UPPER);
			return this;
		}
		
		public PropertyOperand toLowerCase() {
			functionsToApply.addFirst(SqlFunction.LOWER);
			return this;
		}
		
		public PropertyOperand dateToIsoDayOfWeek() {
			functionsToApply.addFirst(SqlFunction.ISO_DAY_OF_WEEK);
			return this;
		}
		
		public PropertyOperand dateToDayOfMonth() {
			functionsToApply.addFirst(SqlFunction.DAY_OF_MONTH);
			return this;
		}
		
		public PropertyOperand dateToDayOfYear() {
			functionsToApply.addFirst(SqlFunction.DAY_OF_YEAR);
			return this;
		}
		
		public PropertyOperand dateToMonth() {
			functionsToApply.addFirst(SqlFunction.MONTH);
			return this;
		}
		
		public PropertyOperand dateToYear() {
			functionsToApply.addFirst(SqlFunction.YEAR);
			return this;
		}
		
		public PropertyOperand dateToIsoWeek() {
			functionsToApply.addFirst(SqlFunction.ISO_WEEK);
			return this;
		}

		public PropertyOperand timeToHour() {
			functionsToApply.addFirst(SqlFunction.HOUR);
			return this;
		}

		public PropertyOperand timeToMinute() {
			functionsToApply.addFirst(SqlFunction.MINUTE);
			return this;
		}

		public PropertyOperand timeToSecond() {
			functionsToApply.addFirst(SqlFunction.SECOND);
			return this;
		}
		
		@Override
		public String toString() {
			return entityName + "." + propertyName;
		}
	}

	public static class PropertyOperation implements Criteria {
		private PropertyOperand left;
		private PropertyOperator operator;
		private Object value;
		
		public PropertyOperation(PropertyOperand left, PropertyOperator operator, Object value) {
			this.left = left;
			this.operator = operator;
			this.value = value;
		}

		public PropertyOperand getLeft() {
			return left;
		}

		public PropertyOperator getOperator() {
			return operator;
		}

		public Object getValue() {
			return value;
		}

		@Override
		public <T> T accept(CriteriaVisitor<T> visitor) {
			return visitor.visit(this);
		}
		
		@Override
		public String toString() {
			return left + " " + operator + " " + value;
		}
	}
	
}
