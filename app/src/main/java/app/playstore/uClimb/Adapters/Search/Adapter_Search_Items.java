package app.playstore.uClimb.Adapters.Search;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.playstore.uClimb.Fragments.Post.custom_post_page;
import app.playstore.uClimb.R;
import app.playstore.uClimb.MVP.MVP_Login.Presenter_Login;

public class Adapter_Search_Items extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Adapter_search_inner";
    private  ArrayList<String> URL= new ArrayList<>();
    private  ArrayList<String> Date   = new ArrayList<>();
    private  ArrayList<String> Location= new ArrayList<>();;
    private  ArrayList<String> info= new ArrayList<>();
    private  ArrayList<String> type_array= new ArrayList<>();
    private  ArrayList<String> post_id= new ArrayList<>();
    private  ArrayList<String>uid= new ArrayList<>();
    private  ArrayList<String>Source_Thumb= new ArrayList<>();



    public Context mContext;


    public Adapter_Search_Items(ArrayList<String> IMG_URL, ArrayList<String> date, ArrayList<String> location, ArrayList<String> info, ArrayList<String> type_array, Context mContext, ArrayList<String> uid, ArrayList<String> post_id) {
        this.URL = IMG_URL;
        Date = date;
        Location = location;
        this.info = info;
        this.type_array = type_array;
        this.mContext = mContext;
        this.uid = uid;
        this.post_id = post_id;
        this.Source_Thumb = Source_Thumb;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"UID_inner" + uid);
        View view =null;
        RecyclerView.ViewHolder viewHolder = null;




        if (i == 0){
            Log.d("adapter_search" , " viewholder_video");
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.private_search_video_item,viewGroup,false);
            viewHolder= new ViewHolder_video(view);


        }
        if (i == 1){
            Log.d("adapter_search" , " viewholder_img");

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.private_search_img_item, viewGroup, false);
            viewHolder= new ViewHolder_img(view);


        }

        return viewHolder;
    }
    @Override
    public int getItemViewType(final int position) {
        int i = 0;
        if (type_array.get(position).equals("IMG")){
            Log.d("Adapter_home","POSITION0: "+position);
            i = 1;
        }
        if(type_array.get(position).equals("video")){
            Log.d("Adapter_home","POSITION1: "+position);

            i = 0;
        }
        return i;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        Log.d("Adapter_search" , "viewtype" + URL );


        //TODO create threads to fight against laggy pages

        if (viewHolder.getItemViewType()== 0) {
            ViewHolder_video viewHolder_video = (ViewHolder_video) viewHolder;
            Log.d(TAG, "Shource:23" + Source_Thumb);
            //if (Source_Thumb.get(i) == null){
            //    setVideoProperties(i, viewHolder_video);


            //}
            //else{
            //Picasso.get().load(Source_Thumb.get(i)).fit().centerCrop().into(viewHolder_video.thumb);
            viewHolder_video.videoView.setVisibility(View.GONE);
            Glide.with(mContext).asBitmap().load(URL.get(i)).centerCrop().into(new CustomTarget<Bitmap>() {

                                                           @Override
                                                           public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                               viewHolder_video.thumb.setImageBitmap(resource);


                                                           }

                                                           @Override
                                                           public void onLoadCleared(@Nullable Drawable placeholder) {

                                                           }

                                                       });
            viewHolder_video.thumb.setVisibility(View.VISIBLE);
            viewHolder_video.thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transactiontopost(i);
                }
            });


                //}
                //viewHolder_video.videoView.setVideoPath(URL.get(i));
                // viewHolder_video.thumb.setVisibility(View.GONE);

                // viewHolder_video.videoView.setVisibility(View.VISIBLE);
                //viewHolder_video.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                //    @Override
                //    public void onCompletion(MediaPlayer mp) {
                //        viewHolder_video.progressBar.setVisibility(View.GONE);

                //    }
                //});


                //      viewHolder_video.videoView.setOnClickListener(new View.OnClickListener() {
                //          @Override
                //          public void onClick(View v) {
                //              Log.d(TAG,"cliockedonce");
                //              transactiontopost(i);


                //          }
                //      });
                //  }
            }
        if (viewHolder.getItemViewType() == 1){
            ViewHolder_img viewHolder_img = (ViewHolder_img) viewHolder;
            viewHolder_img.progressBar.setVisibility(View.VISIBLE);

            Picasso.get().load(URL.get(i)).fit().centerCrop().into(viewHolder_img.img_item_rec_search_page, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    viewHolder_img.progressBar.setVisibility(View.GONE);


                    Log.d(TAG,"loaded");


                }

                @Override
                public void onError(Exception e) {
                    viewHolder_img.progressBar.setVisibility(View.VISIBLE);
                    viewHolder_img.progressBar.setBackgroundTintList(ColorStateList.valueOf(Color.RED));


                }
            });

            viewHolder_img.img_item_rec_search_page.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transactiontopost(i);


                }
            });




        }

    }

    private void setVideoProperties(int i, ViewHolder_video viewHolder_video) {
        Log.d(TAG,"videouri"+URL.get(i));
        viewHolder_video.videoView.seekTo(2);
        viewHolder_video.progressBar.setVisibility(View.VISIBLE);
        viewHolder_video.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                viewHolder_video.progressBar.setVisibility(View.VISIBLE);
                viewHolder_video.progressBar.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return true;

            }
        });
        viewHolder_video.videoView.setVideoPath(URL.get(i));
        viewHolder_video.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        viewHolder_video.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG,"loaded_video");
            }
        });
        viewHolder_video.videoView.seekTo(2);
    }

    private void transactiontopost(int position) {
        Presenter_Login login_presenter = new Presenter_Login();


        custom_post_page mFragment = new custom_post_page();
        Bundle arguments = new Bundle();
        arguments.putString("Source", URL.get(position));
        arguments.putString("UserID", uid.get(position));
        arguments.putString("PostID", post_id.get(position));
        arguments.putString("Type", type_array.get(position));






        FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
        mFragment.setArguments(arguments);
        fragmentManager.beginTransaction().addToBackStack("Fragment_custom_post_page").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container_fragment, mFragment).commit();

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG,"attached:"+ "true");

    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"itemcount" + URL.size());
        return URL.size();
    }

    public class ViewHolder_img extends RecyclerView.ViewHolder {
        ImageView img_item_rec_search_page;
        ProgressBar progressBar;



        public ViewHolder_img(View view) {
            super(view);
            img_item_rec_search_page = view.findViewById(R.id.imageview_item_search_page);
            progressBar = view.findViewById(R.id.progress_item_search_img);
        //   date_txt = (TextView) view.findViewById(R.id.event_custom_txt);
        //   Title_boulderhalle_txt = (TextView) view.findViewById(R.id.event_custom_txt);
        //   Location_txt = (TextView) view.findViewById(R.id.event_custom_txt);
        //   costs_txt = (TextView) view.findViewById(R.id.event_custom_txt);
        //   min_age_txt = (TextView) view.findViewById(R.id.event_custom_txt);
        //   Grade_txt = (TextView) view.findViewById(R.id.event_custom_txt);
        //   event_name_txt = (TextView) view.findViewById(R.id.event_custom_txt);
        //   time_txt = (TextView) view.findViewById(R.id.event_custom_txt);





        }
    }

    public class ViewHolder_video extends RecyclerView.ViewHolder {
        ImageView thumb;
        VideoView videoView;
        ProgressBar progressBar;



        public ViewHolder_video(View view) {
            super(view);
            videoView = view.findViewById(R.id.video_search_item);
            progressBar =view.findViewById(R.id.progress_item_search_video);
            thumb = view.findViewById(R.id.thumb_video_item);
            //   date_txt = (TextView) view.findViewById(R.id.event_custom_txt);
            //   Title_boulderhalle_txt = (TextView) view.findViewById(R.id.event_custom_txt);
            //   Location_txt = (TextView) view.findViewById(R.id.event_custom_txt);
            //   costs_txt = (TextView) view.findViewById(R.id.event_custom_txt);
            //   min_age_txt = (TextView) view.findViewById(R.id.event_custom_txt);
            //   Grade_txt = (TextView) view.findViewById(R.id.event_custom_txt);
            //   event_name_txt = (TextView) view.findViewById(R.id.event_custom_txt);
            //   time_txt = (TextView) view.findViewById(R.id.event_custom_txt);





        }


    }

}
