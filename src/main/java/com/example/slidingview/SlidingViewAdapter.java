package com.example.slidingview;

import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 13-6-18.
 */
public class SlidingViewAdapter {

    protected int row;

    protected int col;

    protected List<ItemInfo> data;


    public SlidingViewAdapter(List<ItemInfo> data, int row, int col) {
        this.data = data;
        this.row = row;
        this.col = col;
    }

    public void setData(List<ItemInfo> data) {
        this.data = data;
    }

    public int getPageNum(){
        if(data != null && data.size() > 0){
            int pageNum = (int)(data.size()/(row*col));
            if(data.size()%(row*col) > 0){
                pageNum = pageNum + 1;
            }
            return pageNum;
        }
        return 0;
    }

    /**
     * 得到指定页数的数据 从0开始
     * @param page
     * @return
     */
    public List<ItemInfo> getDataInPage(int page){
        if(page > getPageNum()-1){
            return null;
        }

        int start = page*(col*row);
        int end = (page+1)*col*row;
        end = Math.min(end, data.size());
        return data.subList(start, end);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getPageSize(){
        return col*row;
    }
}
