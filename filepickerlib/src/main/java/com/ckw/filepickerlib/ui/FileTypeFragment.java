package com.ckw.filepickerlib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ckw.filepickerlib.R;
import com.ckw.filepickerlib.adapter.PathAdapter;
import com.ckw.filepickerlib.filter.LFileFilter;
import com.ckw.filepickerlib.model.ParamEntity;
import com.ckw.filepickerlib.utils.FileUtils;
import com.ckw.filepickerlib.widget.EmptyRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ckw
 * on 2021/10/15.
 */
public class FileTypeFragment extends Fragment {

    public static FileTypeFragment newInstance(int type, ParamEntity paramEntity) {

        Bundle args = new Bundle();
        args.putInt("type",type);
        args.putSerializable("param", paramEntity);
        FileTypeFragment fragment = new FileTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private View rootView;
    private int mType;

    private View mEmptyView;
    private EmptyRecyclerView mRecylerView;
    private ArrayList<String> mListNumbers = new ArrayList<String>();//存放选中条目的数据地址
    private PathAdapter mPathAdapter;
    private LFileFilter mFilter;
    private String mPath;
    private ParamEntity mParamEntity;
    private List<File> mListFiles;
    private TextView mTvPath;
    private TextView mTvBack;
    private LinearLayout mLayoutPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_type_file, container, false);
        }
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mType = getArguments().getInt("type");
        mParamEntity = (ParamEntity) getArguments().getSerializable("param");
        switch (mType){
            case 1:
                mPath = "/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download/";
                break;
            case 2:
                mPath = "/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/";
                break;
            case 3:
                mPath = "/storage/emulated/0/DingTalk/";
                break;
            case 4:
                mPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                break;
        }
        mLayoutPath = rootView.findViewById(R.id.layout_path);
        mLayoutPath.setVisibility(View.GONE);
        mTvBack = (TextView) rootView.findViewById(R.id.tv_back);
        mTvPath = (TextView) rootView.findViewById(R.id.tv_path);
        mTvPath.setText(mPath);
        mRecylerView =  rootView.findViewById(R.id.recylerview_file);
        mEmptyView = rootView.findViewById(R.id.empty_view);
        mFilter = new LFileFilter(mParamEntity.getFileTypes());
        mListFiles = FileUtils.getFileList(mPath, mFilter, mParamEntity.isGreater(), mParamEntity.getFileSize());
        mPathAdapter = new PathAdapter(mListFiles, getActivity(), mFilter, mParamEntity.isMutilyMode(), mParamEntity.isGreater(), mParamEntity.getFileSize());
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mPathAdapter.setmIconStyle(mParamEntity.getIconStyle());
        mRecylerView.setAdapter(mPathAdapter);
        mRecylerView.setmEmptyView(mEmptyView);
        initListener();
    }

    /**
     * 显示顶部地址
     *
     * @param path
     */
    private void setShowPath(String path) {
        mTvPath.setText(path);
    }

    /**
     * 添加点击事件处理
     */
    private void initListener() {
        // 返回目录上一级
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempPath = new File(mPath).getParent();
                if (tempPath == null) {
                    return;
                }

                switch (mType){
                    case 1:
                        if(tempPath.equals("/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download")){
                            mLayoutPath.setVisibility(View.GONE);
                        }
                        if(mPath.equals("/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download")){
                            return;
                        }
                        break;
                    case 2:
                        if(tempPath.equals("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv")){
                            mLayoutPath.setVisibility(View.GONE);
                        }
                        if(mPath.equals("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv")){
                            return;
                        }
                        break;
                    case 3:

                        if(tempPath.equals("/storage/emulated/0/DingTalk")){
                            mLayoutPath.setVisibility(View.GONE);
                        }
                        if(mPath.equals("/storage/emulated/0/DingTalk")){
                            return;
                        }
                        break;
                    case 4:

                        if(tempPath.equals("/storage/emulated/0")){
                            mLayoutPath.setVisibility(View.GONE);
                        }
                        if(mPath.equals("/storage/emulated/0")){
                            return;
                        }
                        break;
                }
                mPath = tempPath;
                mListFiles = FileUtils.getFileList(mPath, mFilter, mParamEntity.isGreater(), mParamEntity.getFileSize());
                mPathAdapter.setmListData(mListFiles);
                mPathAdapter.updateAllSelelcted(false);
                mRecylerView.scrollToPosition(0);
                setShowPath(mPath);
                //清除添加集合中数据
                mListNumbers.clear();
            }
        });
        mPathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
            @Override
            public void click(int position) {

                //单选模式直接返回
                if (mListFiles.get(position).isDirectory()) {
                    //进入下一层文件夹
                    mLayoutPath.setVisibility(View.VISIBLE);
                    chekInDirectory(position);
                    return;
                }
                if (mParamEntity.isChooseMode()) {
                    //选择文件模式,需要添加文件路径，否则为文件夹模式，直接返回当前路径
                    mListNumbers.add(mListFiles.get(position).getAbsolutePath());
                    chooseDone();
                } else {
                    Toast.makeText(getActivity(), R.string.lfile_ChooseTip, Toast.LENGTH_SHORT).show();
                }
            }


        });

    }

    /**
     * 完成提交
     */
    private void chooseDone() {
        //判断是否数量符合要求
        if (mParamEntity.isChooseMode()) {
            if (mParamEntity.getMaxNum() > 0 && mListNumbers.size() > mParamEntity.getMaxNum()) {
                Toast.makeText(getActivity(), R.string.lfile_OutSize, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("paths", mListNumbers);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    /**
     * 点击进入目录
     *
     * @param position
     */
    private void chekInDirectory(int position) {
        mPath = mListFiles.get(position).getAbsolutePath();
        //更新数据源
        mListFiles = FileUtils.getFileList(mPath, mFilter, mParamEntity.isGreater(), mParamEntity.getFileSize());
        mPathAdapter.setmListData(mListFiles);
        mPathAdapter.notifyDataSetChanged();
        mRecylerView.scrollToPosition(0);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
//            mIsFirstLoad = true;
//            mIsPrepare = false;
//            mIsVisible = false;
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

}