package ru.zim.ates.auth.mapper;

import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import ru.zim.ates.auth.util.StreamUtil;
import ru.zim.ates.common.model.AppRole;

@Converter
public class CommaSeparatedStringsToRolesConverter implements AttributeConverter<List<AppRole>, String> {
    @Override
    public String convertToDatabaseColumn(List<AppRole> list) {
        return listToString(list);
    }

    @Override
    public List<AppRole> convertToEntityAttribute(String joined) {
        return StreamUtil.map(Arrays.asList(joined.split(",")), it -> Enum.valueOf(AppRole.class, it.trim()));
    }

    public static String listToString(List<AppRole> list) {
        return list == null ? "" : String.join(",", StreamUtil.map(list, Enum::name));
    }

    public static String[] listToArray(List<AppRole> list) {
        return list == null ? new String[]{} : StreamUtil.map(list, Enum::name).toArray(new String[]{});
    }
}
