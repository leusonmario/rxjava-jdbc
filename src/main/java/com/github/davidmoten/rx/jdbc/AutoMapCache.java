package com.github.davidmoten.rx.jdbc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.github.davidmoten.rx.jdbc.Util.Col;
import com.github.davidmoten.rx.jdbc.Util.IndexedCol;
import com.github.davidmoten.rx.jdbc.Util.NamedCol;
import com.github.davidmoten.rx.jdbc.annotations.Column;
import com.github.davidmoten.rx.jdbc.annotations.Index;

class AutoMapCache {
    final Map<String, Col> methodCols;
    public Class<?> cls;

    AutoMapCache(Class<?> cls) {
        this.cls = cls;
        this.methodCols = getMethodCols(cls);
    }
    
    private static Map<String, Col> getMethodCols(Class<?> cls) {
        Map<String, Col> methodCols = new HashMap<String, Col>();
        for (Method method : cls.getMethods()) {
            String name = method.getName();
            Column column = method.getAnnotation(Column.class);
            if (column != null) {
                //TODO check method has no params and has a mappable return type
                methodCols.put(name, new NamedCol(column.value(), method.getReturnType()));
            } else {
                Index index = method.getAnnotation(Index.class);
                if (index != null) {
                    //TODO check method has no params and has a mappable return type
                    methodCols.put(name, new IndexedCol(index.value(), method.getReturnType()));
                }
            }
        }
        return methodCols;
    }

    
}
