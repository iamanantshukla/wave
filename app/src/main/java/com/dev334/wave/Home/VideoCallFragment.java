package com.dev334.wave.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.wave.Database.TinyDB;
import com.dev334.wave.MeetingAdapter;
import com.dev334.wave.MeetingModel;
import com.dev334.wave.NewMeeting;
import com.dev334.wave.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import id.zelory.compressor.Compressor;


public class VideoCallFragment extends Fragment implements MeetingAdapter.SelectedPager {

    EditText secretCode;
    Button joinBtn;
    RecyclerView MeetingRecycler;
    private List<MeetingModel> meetingModels;
    private MeetingAdapter meetingAdapter;
    private TinyDB tinyDB;
    private Map<String, Object> map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_call, container, false);
        MeetingRecycler=view.findViewById(R.id.MeetingRecycler);
        //secretCode=view.findViewById(R.id.code);
        joinBtn=view.findViewById(R.id.createNewBtn);
        URL serverURL;
        map=new HashMap<>();

        tinyDB=new TinyDB(getContext());
        map=tinyDB.getObject("UserProfile",map.getClass());

        try{
            serverURL=new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL).
                            setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), NewMeeting.class);
                i.putExtra("Org",map.get("Organisation").toString());
                startActivity(i);
            }
        });

        meetingModels=new ArrayList<>();

        meetingAdapter=new MeetingAdapter(meetingModels,this);
        MeetingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        MeetingRecycler.setHasFixedSize(true);
        MeetingRecycler.setAdapter(meetingAdapter);

        fetchCurrentMeetings();

        return view;
    }

    private void fetchCurrentMeetings() {
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        Log.i("Meetings", "fetchCurrentMeetings: "+map.get("Organisation").toString());
        firebaseFirestore.collection("Meeting").whereEqualTo("Org",map.get("Organisation").toString())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.i("Meeting", "onSuccess: "+queryDocumentSnapshots.size());
                for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                    meetingModels.add(snapshot.toObject(MeetingModel.class));
                    meetingAdapter.notifyDataSetChanged();
                }
                meetingAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

            }

    @Override
    public void selectedpager(MeetingModel meetingModel) {
//        Toast.makeText(getContext(), meetingModel.getCode(), Toast.LENGTH_SHORT).show();
        JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                .setRoom(meetingModel.getCode().toString())
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeetActivity.launch(getContext(),options);
    }
}