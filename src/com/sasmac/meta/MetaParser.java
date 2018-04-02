package com.sasmac.meta;
import com.sasmac.meta.spatialmetadata;;

public interface MetaParser {
	spatialmetadata ParseMeta(String strxmlFile) throws Exception;
}
