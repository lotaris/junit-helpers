package com.forbesdigital.junit.helpers;

/**
 * Helper class to reflect the attributes of an annotation contained within
 * another annotation.
 * 
 * @author Alexandru Obaj <alexandru.obaj@fortech.ro>
 */
public class AttributeOverrideDetails {
	private String name;
		private String columnName;
		private Boolean columnUnique;
		private Boolean columnNullable;
		private Boolean columnInsertable;
		private Boolean columnUpdatable;
		private String columnDefinition;
		private String columnTable;
		private Integer columnLength;
		private Integer columnPrecision;
		private Integer columnScale;
		
		// <editor-fold defaultstate="collapsed" desc="Getters & Setters">
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public Boolean getColumnUnique() {
			return columnUnique;
		}

		public void setColumnUnique(Boolean columnUnique) {
			this.columnUnique = columnUnique;
		}

		public Boolean getColumnNullable() {
			return columnNullable;
		}

		public void setColumnNullable(Boolean columnNullable) {
			this.columnNullable = columnNullable;
		}

		public Boolean getColumnInsertable() {
			return columnInsertable;
		}

		public void setColumnInsertable(Boolean columnInsertable) {
			this.columnInsertable = columnInsertable;
		}

		public Boolean getColumnUpdatable() {
			return columnUpdatable;
		}

		public void setColumnUpdatable(Boolean columnUpdatable) {
			this.columnUpdatable = columnUpdatable;
		}

		public String getColumnDefinition() {
			return columnDefinition;
		}

		public void setColumnDefinition(String columnDefinition) {
			this.columnDefinition = columnDefinition;
		}

		public String getColumnTable() {
			return columnTable;
		}

		public void setColumnTable(String columnTable) {
			this.columnTable = columnTable;
		}

		public Integer getColumnLength() {
			return columnLength;
		}

		public void setColumnLength(Integer columnLength) {
			this.columnLength = columnLength;
		}

		public Integer getColumnPrecision() {
			return columnPrecision;
		}

		public void setColumnPrecision(Integer columnPrecision) {
			this.columnPrecision = columnPrecision;
		}

		public Integer getColumnScale() {
			return columnScale;
		}

		public void setColumnScale(Integer columnScale) {
			this.columnScale = columnScale;
		}
		// </editor-fold>
		
}
