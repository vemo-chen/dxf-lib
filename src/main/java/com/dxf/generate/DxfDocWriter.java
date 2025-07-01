package com.dxf.generate;

import com.dxf.generate.model.BaseDxfEntity;
import com.dxf.generate.model.DxfEntity;
import com.dxf.generate.model.Vector3;
import com.dxf.generate.model.entities.*;
import com.dxf.generate.utils.DxfUtil;
import com.dxf.generate.utils.StreamUtil;
import com.dxf.generate.utils.StringUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

/**
 * Dxf处理类，支持向矿图中添加自定义图形，目前支持的图形有圆形Circle，圆弧Arc，直线Line，多线段LwPolyLine，文字Text
 * 目前仅仅支持对圆形Circle和其子类Arc的颜色填充。 可以通过调用 {@link #addEntity(DxfEntity)} 和 {@link #removeEntity(DxfEntity)}
 * 进行自定义图元的添加和删除。
 * <p>
 * 并支持对新dxf文件的导出，可以选择调用函数 {@link #save(String, boolean)} 导出所有文件，这样的文件可以在AutoCad中打开
 * 也可以通过函数 {@link #saveEntities(String, boolean)} 仅仅导出entity图元信息，这样的文件仅仅支持使用解析库进行解析
 * <p>
 * 另外和文件流一样，dxfDocument对象也需要在使用结束后关闭，通过调用 {@link #close()} 函数进行关闭，可以以想流式对象那样使用，如
 * <blockquote><pre>
 *     try(DxfDocument dxf = new DxfDocument(path)) {
 *         body
 *     }
 * </pre></blockquote>
 *
 * @author wangp
 */
public class DxfDocWriter implements Closeable {

    /**
     * 当选择保存为dxf文件的时候，要保留的Entities类型的数组，不在数组中的对象将会被忽略掉（不包含自定义加入的图元）
     */
    public static final String[] DEFAULT_ENTITY_NO_REDUCE_PART = {"LINE", "CIRCLE", "ARC", "TEXT", "MTEXT", "LWPOLYLINE", "POLYLINE"};
    /**
     * 空的DXF文件模板路径
     */
    public static final String DEFAULT_EMPTY_DXF_PATH = "dxf/empty3d.dxf";
    private static final Logger log = Logger.getLogger(DxfDocWriter.class.getName());
    private List<String> entityNoReducePart = Arrays.asList(DEFAULT_ENTITY_NO_REDUCE_PART);
    private final List<DxfEntity> newDxfEntityList;
    private final List<DxfEntity> newDxfLayerList;
    private long maxMeta;
    private final Charset charset;

    private BufferedReader br;
    private File dxfFile;

    /**
     * 加载dxf文件，默认的文件加载编码为UFT8
     *
     * @param dxfFilePath dxf文件位置
     */
    public DxfDocWriter(String dxfFilePath) {
        this(dxfFilePath, StandardCharsets.UTF_8);
    }

    /**
     * 以指定编码格式加载dxf文件
     *
     * @param dxfFilePath dxf文件位置
     * @param charset     文件编码
     * @see StandardCharsets
     */
    public DxfDocWriter(String dxfFilePath, Charset charset) {
        this.charset = charset;
        newDxfEntityList = new ArrayList<>();
        newDxfLayerList = new ArrayList<>();
        if (dxfFilePath != null) {
            this.dxfFile = new File(dxfFilePath);
            try {
                this.br = StreamUtil.getFileReader(dxfFile.getAbsolutePath(), charset);
            } catch (FileNotFoundException e) {
                log.severe("file " + dxfFilePath + "not exists");
            }
            maxMeta = DxfUtil.readMaxMeta(dxfFilePath);
        } else {
            maxMeta = Long.parseLong("2AC", 16);
        }
    }

    /**
     * 创建一个空的dxf文件
     */
    public DxfDocWriter() {
        this(null, StandardCharsets.UTF_8);
    }

    /**
     * 向矿图文件中添加一个图元，目前仅仅支持  {@link DxfCircle},{@link DxfArc},{@link DxfLine},{@link DxfLwPolyLine},{@link DxfText}
     * <p>
     * 也可以通过继承 {@link BaseDxfEntity} 或实现 {@link DxfEntity} 接口来扩展图元
     *
     * @param dxfEntity 图元
     */
    public void addEntity(DxfEntity dxfEntity) {
        if (dxfEntity instanceof DxfRay && Vector3.ZERO.equals(((DxfRay) dxfEntity).getDirection())) {
            log.severe(dxfEntity.getEntityName() + " direction cant be zero!!, will ignore this entity");
            return;
        }

        if (dxfEntity instanceof DxfLwPolyLine && ((DxfLwPolyLine) dxfEntity).isEmpty()) {
            log.warning("LwPolyLine not contains any point, will ignore this entity");
            return;
        }

        if (dxfEntity instanceof DxfPolyLine && ((DxfPolyLine) dxfEntity).isEmpty()) {
            log.warning("PolyLine not contains any point, will ignore this entity");
            return;
        }

        if (dxfEntity.getMeta() == null) {
            dxfEntity.setMeta(++maxMeta);
        }
        this.newDxfEntityList.add(dxfEntity);

        if ((dxfEntity instanceof DxfCircle || dxfEntity instanceof DxfLwPolyLine) && ((BaseDxfEntity) dxfEntity).isSolid()) {
            DxfHatch dxfHatch = DxfHatch.buildHatchBy((BaseDxfEntity) dxfEntity, ++maxMeta);
            addEntity(dxfHatch);
            dxfEntity.setReactors(DxfUtil.formatMeta(dxfHatch.getMeta()));
        }

        if ((dxfEntity instanceof DxfPolyLine)) {
            // 提前跳过一位留给vertex结束后使用
            ++maxMeta;
            ((DxfPolyLine) dxfEntity).buildVertex((BaseDxfEntity) dxfEntity, ++maxMeta);
            // meta 偏移 顶点数量
            maxMeta += ((DxfPolyLine) dxfEntity).getPoints().size();
        }

    }

    /**
     * 添加实体
     **/
    public void addEntity(List<DxfEntity> dxfEntityList) {
        for (DxfEntity entity : dxfEntityList) {
            addEntity(entity);
        }
    }

    /**
     * 添加一个自定义的图层
     * <p>
     * 图层的句柄值会自动生成，如果图层中没有设置句柄值，那么会自动设置为当前最大句柄值+1
     *
     * @param dxfEntity 图层
     */
    public void addLayer(DxfEntity dxfEntity) {
        dxfEntity.setMeta(++maxMeta);
        this.newDxfLayerList.add(dxfEntity);
    }

    /**
     * 添加图层
     *
     * @param dxfEntityList 图层列表
     */
    public void addLayer(List<DxfEntity> dxfEntityList) {
        for (DxfEntity entity : dxfEntityList) {
            addLayer(entity);
        }
    }

    /**
     * 移除一个自定义的图元
     *
     * @param dxfEntity 图元
     */
    public void removeEntity(DxfEntity dxfEntity) {
        this.newDxfEntityList.remove(dxfEntity);
        for (DxfEntity entity : newDxfEntityList) {
            if (entity instanceof DxfHatch) {
                if (((DxfHatch) dxfEntity).getDxfSolid().getDxfEntity() == dxfEntity) {
                    this.newDxfEntityList.remove(dxfEntity);
                }
            }
        }
    }

    /**
     * 设置要保存的矿图文件中要保留的图元类型列表，如果设置为 null, 那么原矿图中的所有图元都将会被保留
     * 默认的要保存的图元列表表为 {@link #DEFAULT_ENTITY_NO_REDUCE_PART}
     *
     * @param entityNoReducePart 要保留的图元列表
     */
    public void setEntityNoReducePart(String... entityNoReducePart) {
        if (entityNoReducePart == null) {
            this.entityNoReducePart = null;
        } else {
            this.entityNoReducePart = Arrays.asList(entityNoReducePart);
        }
    }

    /**
     * 仅仅保存矿图中的图元信息（ENTITIES 部分）到文件中，其他部分（HEADER， CLASSES， TABLES...）都将被舍弃
     * 通过 {@link #setEntityNoReducePart(String...)} 可以自定义保留那些类型的图元
     *
     * @param entitiesFilePath 要保存的文件位置
     * @param containNewEntity 是否包含自定义添加进去的图元信息
     */
    public void saveEntities(String entitiesFilePath, boolean containNewEntity) {
        save(entitiesFilePath, true, containNewEntity);
    }

    /**
     * 保存为新的dxf文件
     *
     * @param saveDxfFilePath  要保存的问题件位置
     * @param containNewEntity 是否报刊自定义添加进去的图元信息
     */
    public void save(String saveDxfFilePath, boolean containNewEntity) {
        save(saveDxfFilePath, false, containNewEntity);
    }

    public String readString(boolean containNewEntity) {
        StringBuilder result = new StringBuilder();
        read(false, containNewEntity, result::append);
        return result.toString();
    }

    private void read(boolean justSaveEntity, boolean containNewEntity, ReadDxfConsumer readStr) {
        try {
            if (dxfFile != null) {
                br.mark((int) (dxfFile.length() + 1));
            } else {
                br = StreamUtil.getReader(StreamUtil.getResourceStream(DEFAULT_EMPTY_DXF_PATH));
            }
            StringBuffer writeBuffer = new StringBuffer();
            while (true) {
                String[] pair = StreamUtil.readNextPair(br);
                if (pair == null) {
                    break;
                }
                if ("0".equals(pair[0].trim()) && "SECTION".equals(pair[1].trim())) {
                    // nextPartStart
                    String[] nextPairTitleTag = StreamUtil.readNextPair(br);
                    if (nextPairTitleTag == null) {
                        continue;
                    }
                    String nextPartName = nextPairTitleTag[1].trim();
                    if (justSaveEntity) {
                        justSaveEntity(writeBuffer, pair, nextPairTitleTag, containNewEntity);
                    } else {
                        handleAll(writeBuffer, pair, nextPairTitleTag, containNewEntity);
                    }
                    readStr.accept(writeBuffer.toString());
                    writeBuffer = new StringBuffer();
                    log.info("handle part " + nextPartName + " end");
                } else if ("EOF".equals(pair[1].trim())) {
                    StringUtil.appendLnCrLf(writeBuffer, pair);
                    readStr.accept(writeBuffer.toString());
                    log.info("completed!! dxf reduce end<<<<<<<<<<<<<");
                    break;
                }
            }
        } catch (Exception e) {
            log.severe("Reduce dxf fail!!!!!!" + e.getMessage());
        } finally {
            try {
                if (dxfFile != null) {
                    if (br != null) {
                        br.reset();
                    }
                } else {
                    if (br != null) {
                        br.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void save(String saveDxfFilePath, boolean justSaveEntity, boolean containNewEntity) {
        try (BufferedWriter fileWriter = StreamUtil.getFileWriter(saveDxfFilePath, charset)) {
            read(justSaveEntity, containNewEntity, fileWriter::write);
            fileWriter.flush();
        } catch (IOException e) {
            log.severe("save fail" + e.getMessage());
        }

    }

    private void justSaveEntity(StringBuffer writeBuffer, String[] pair, String[] nextPairTitleTag,
                                boolean containNewEntity) throws IOException {
        String nextPartName = nextPairTitleTag[1].trim();
        if ("ENTITIES".equals(nextPartName)) {
            StringUtil.appendLnCrLf(writeBuffer, pair);
            StringUtil.appendLnCrLf(writeBuffer, nextPairTitleTag);
            handEntities(writeBuffer, containNewEntity);
        } else {
            ignoreNextPart();
        }
    }

    private void handleAll(StringBuffer writeBuffer, String[] pair,
                           String[] nextPairTitleTag, boolean containNewEntity) throws IOException {
        String nextPartName = nextPairTitleTag[1].trim();
        switch (nextPartName) {
            case "HEADER":
                StringUtil.appendLnCrLf(writeBuffer, pair);
                StringUtil.appendLnCrLf(writeBuffer, nextPairTitleTag);
                handHeader(writeBuffer);
                break;
            case "ENTITIES":
                StringUtil.appendLnCrLf(writeBuffer, pair);
                StringUtil.appendLnCrLf(writeBuffer, nextPairTitleTag);
                handEntities(writeBuffer, containNewEntity);
                break;
            case "TABLES":
                StringUtil.appendLnCrLf(writeBuffer, pair);
                StringUtil.appendLnCrLf(writeBuffer, nextPairTitleTag);
                handTables(writeBuffer);
                break;
            case "CLASSES":
            case "BLOCKS":
            case "OBJECTS":
                StringUtil.appendLnCrLf(writeBuffer, pair);
                StringUtil.appendLnCrLf(writeBuffer, nextPairTitleTag);
                justOutput(writeBuffer);
                break;
            default:
                ignoreNextPart();
                break;
        }
    }

    private void ignoreNextPart() throws IOException {
        while (true) {
            String[] piar = StreamUtil.readNextPair(br);
            if (piar == null) {
                break;
            }
            if ("ENDSEC".equals(piar[1].trim())) {
                // header end, endsec is the end tag
                break;
            }
        }
    }

    private void justOutput(StringBuffer writeBuffer) throws IOException {
        log.info("part keep raw info");
        while (true) {
            String[] pair = StreamUtil.readNextPair(br);
            if (pair == null) {
                break;
            }
            StringUtil.appendLnCrLf(writeBuffer, pair);
            if ("ENDSEC".equals(pair[1].trim())) {
                // header end, endsec is the end tag
                break;
            }
        }
    }

    private void handEntities(StringBuffer writeBuffer, boolean containNewEntity) throws IOException {
        Map<String, Integer> keepMapResult = new HashMap<>(4);
        Map<String, Integer> ignoreMapResult = new HashMap<>(4);
        String[] nextPair = StreamUtil.readNextPair(br);
        while (true) {
            if (nextPair == null) {
                break;
            }
            if ("ENDSEC".equals(nextPair[1].trim())) {
                // read entities end
                if (containNewEntity) {
                    appendNewEntities(writeBuffer);
                }
                StringUtil.appendLnCrLf(writeBuffer, nextPair);
                break;
            } else {
                // read entities body
                String nextEntityName = nextPair[1].trim();
                boolean isReduced = entityNoReducePart != null && !entityNoReducePart.contains(nextEntityName);
                if (!isReduced) {
                    StringUtil.appendLnCrLf(writeBuffer, nextPair);
                    keepMapResult.put(nextEntityName, keepMapResult.getOrDefault(nextEntityName, 0) + 1);
                } else {
                    ignoreMapResult.put(nextEntityName, ignoreMapResult.getOrDefault(nextEntityName, 0) + 1);
                }
                nextPair = readNextEntity(writeBuffer, isReduced);
            }
        }
        log.info("[Entities]keep=>" + keepMapResult + "ignore=>" + ignoreMapResult);
    }

    private void appendNewEntities(StringBuffer buffer) {
        for (DxfEntity dxfEntity : newDxfEntityList) {
            buffer.append(dxfEntity.getDxfStr());
        }
    }

    /**
     * add not reduce tag content to writeBuffer, and return next entity title tag
     *
     * @param writeBuffer content buffer
     * @return next entity title tag
     * @throws IOException if read file error throw this exception
     */
    private String[] readNextEntity(StringBuffer writeBuffer, boolean isReduced) throws IOException {
        // check tag is need to reduce
        while (true) {
            String[] pair = StreamUtil.readNextPair(br);
            if (pair == null) {
                return null;
            }
            // code equals zero mean meet next entity tag title, and mean this entity end
            if ("0".equals(pair[0].trim())) {
                return pair;
            }
            if (!isReduced) {
                StringUtil.appendLnCrLf(writeBuffer, pair);
            }
        }
    }

    private void handHeader(StringBuffer writeBuffer) throws IOException {
        while (true) {
            String[] pair = StreamUtil.readNextPair(br);
            if (pair == null) {
                break;
            }
            StringUtil.appendLnCrLf(writeBuffer, pair);
            if (!newDxfEntityList.isEmpty() && DxfUtil.isPairNameEquals(pair, "$HANDSEED")) {
                pair = StreamUtil.readNextPair(br);
                if (pair == null) {
                    return;
                }
                if ("5".equals(pair[0].trim())) {
                    pair[1] = DxfUtil.formatMeta(maxMeta + 2);
                }
                StringUtil.appendLnCrLf(writeBuffer, pair);
            }
            if ("ENDSEC".equals(pair[1].trim())) {
                // header end, endsec is the end tag
                break;
            }
        }
    }

    private void handTables(StringBuffer writeBuffer) throws IOException {
        while (true) {
            String[] pair = StreamUtil.readNextPair(br);
            if (pair == null) {
                break;
            }
            StringUtil.appendLnCrLf(writeBuffer, pair);
            if ("2".equals(pair[0].trim()) && "LAYER".equals(pair[1].trim())) {
                while (true) {
                    String[] layerPair = StreamUtil.readNextPair(br);
                    if (layerPair == null) {
                        break;
                    }
                    if ("0".equals(layerPair[0].trim()) && "ENDTAB".equals(layerPair[1].trim())) {
                        appendNewLayer(writeBuffer);
                        // layer end
                        StringUtil.appendLnCrLf(writeBuffer, layerPair);
                        break;
                    }
                    StringUtil.appendLnCrLf(writeBuffer, layerPair);
                }
            }
            if ("ENDSEC".equals(pair[1].trim())) {
                // header end, endsec is the end tag
                break;
            }
        }
    }

    private void appendNewLayer(StringBuffer buffer) {
        for (DxfEntity dxfEntity : newDxfLayerList) {
            buffer.append(dxfEntity.getDxfStr());
        }
    }


    /**
     * close
     *
     * @throws IOException IOException
     */
    @Override
    public void close() throws IOException {
        StreamUtil.closeStream(br);
        log.info("Dxf document has been closed.");
    }

    interface ReadDxfConsumer {
        void accept(String string) throws IOException;
    }
}
