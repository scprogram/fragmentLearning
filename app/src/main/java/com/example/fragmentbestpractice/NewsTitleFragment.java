package com.example.fragmentbestpractice;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {
    private boolean isTwoPane;

    public NewsTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater
                .inflate(R.layout.news_title_fragment,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.news_title_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        NewsTitleFragment.NewsAdapter adapter=new NewsTitleFragment.NewsAdapter(getNews());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<News> getNews(){
        List<News> newsList=new ArrayList<>();
        for(int i=1;i<=50;i++){
            News news=new News();
            news.setTitle("This is new title"+i);
            news.setContent(getRandomLengthContent("this is news content"+i+" "));
            newsList.add(news);
        }
        return newsList;
    }

    private String getRandomLengthContent(String content){
        Random random=new Random();
        int length=random.nextInt(5)+1;
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<length;i++)
            builder.append(content);

        return builder.toString();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content_layout)!=null)
            isTwoPane=true;
        else
            isTwoPane=false;
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.newsView>{
        private List<News> newsList;
        class newsView extends RecyclerView.ViewHolder{
            TextView newsTitleText;
            public newsView(View view){
                super(view);
                newsTitleText=view.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter(List<News> newsList) {
            this.newsList=newsList;
        }

        @NonNull
        @Override
        public newsView onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.news_item,viewGroup,false);
            final newsView finalNews=new newsView(view);
            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    News news=newsList.get(finalNews.getAdapterPosition());
                    if(isTwoPane){
                        //双页模式
                        NewsContentFragment newsContentFragment=
                                (NewsContentFragment)getFragmentManager()
                                .findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(),news.getContent());
                    }
                    else{
                        //单页模式
                        NewsContentActivity.actionStart(getActivity(),
                                news.getTitle(),news.getContent());
                    }
                }
            });
            return finalNews;
        }

        @Override
        public void onBindViewHolder(@NonNull newsView newsView, int i) {
            News news=newsList.get(i);
            newsView.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }
    }
}
