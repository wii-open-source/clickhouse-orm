package org.wii.clickhouse.orm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:05
 */
@Slf4j
public class CheckErrorUtil {
    private static final String TEMPLATE_SYNTAX = "@{";

    public static void illegalSql(String sql){
        if (!sql.contains(TEMPLATE_SYNTAX)){
            throw new IllegalArgumentException("sql template express syntax err");
        }
    }

    public static void illegalExpress(String express){
        if (!express.contains(TEMPLATE_SYNTAX)){
            throw new IllegalArgumentException("template express syntax err");
        }
    }

    public static void sqlSyntaxErr(String sql){
        if (sql.contains(TEMPLATE_SYNTAX)){
            log.info("SQL Syntax Error:[" + sql + "]");
            throw new IllegalArgumentException("sql syntax err");
        }
    }

    public static void paramsErr(List<?> list){
        if (CollectionUtils.isEmpty(list)){
            throw new IllegalArgumentException("Params Not Empty");
        }
    }

    public static void nonAnnotationClass(Class<?> clazz){
        Table table = clazz.getAnnotation(Table.class);
        if (null == table){
            log.info("Class Not Annotation Error");
            throw new IllegalArgumentException("Class Not Annotation Error");
        }
        if (StringUtils.isBlank(table.name())){
            throw new IllegalArgumentException("Class Table Annotation Error");
        }
        List<Field> list = FieldUtil.getAllFields(clazz, null, null);
        for (Field field: list){
            Transient trans = field.getAnnotation(Transient.class);
            if (trans != null){
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (null == column){
                throw new IllegalArgumentException("Class Column Not Annotation Error");
            }

            if (StringUtils.isBlank(column.name())){
                throw new IllegalArgumentException("Class Column Annotation Error");
            }
        }
    }

}
