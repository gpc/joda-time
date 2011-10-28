package com.energizedwork.grails.plugins.jodatime.simpledatastore

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.grails.datastore.mapping.simple.query.SimpleMapResultList

abstract class SimpleMapJodaTimeMarshaller<T> extends AbstractMappingAwareCustomTypeMarshaller<T, Map, SimpleMapResultList> {

	SimpleMapJodaTimeMarshaller(Class<T> targetType) {
		super(targetType)
	}

	@Override
	protected Object writeInternal(PersistentProperty property, String key, T value, Map nativeTarget) {
		nativeTarget[key] = value
	}

	@Override
	protected T readInternal(PersistentProperty property, String key, Map nativeSource) {
		nativeSource[key]
	}

	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, SimpleMapResultList nativeQuery) {
		def op = criterion.getClass()
		switch (op) {
			case Query.Equals:
			case Query.NotEquals:
			case Query.GreaterThan:
			case Query.GreaterThanEquals:
			case Query.LessThan:
			case Query.LessThanEquals:
			case Query.Between:
				Closure handler = nativeQuery.query.handlers[op]
				nativeQuery.results << handler.call(criterion, property)
				break
			default:
				throw new RuntimeException("unsupported query type $criterion for property $property")
		}
	}

}
