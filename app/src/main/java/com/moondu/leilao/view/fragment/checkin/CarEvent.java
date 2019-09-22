package com.moondu.leilao.view.fragment.checkin;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moondu.leilao.R;
import com.moondu.leilao.model.adapter.AudioAdapter;
import com.moondu.leilao.model.adapter.PhotoAdapter;
import com.moondu.leilao.model.adapter.TipoOcorrenciaAdapter;
import com.moondu.leilao.model.adapter.VideoAdapter;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.base.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class CarEvent extends BaseFragment {

    private ServiceOrder item;
    private Integer position;

    private Spinner spTipoOcorrencia;
    private RecyclerView rbTipoOcorrencia;
    private RecyclerView listImages;
    private RecyclerView listVideos;
    private EditText ipDescricao;
    private EditText ipMotivoRecolhimento;

    static final int REQUEST_VIDEO_CAPTURE = 902;
    static final int REQUEST_IMAGE_CAPTURE = 901;

    private String imagePath;
    private String videoPath;

    private Integer photoIndex;


    private List<String> ocorrencias;

    Dialog myDialog;

    @Override
    public void onCreate(@Nullable Bundle instance) {
        super.onCreate(instance);

        if (getArguments() != null) {
            position = getArguments().getInt(BaseFragment.INDEX);
            item = (ServiceOrder) getArguments().getSerializable(ServiceOrder.class.getName());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.car_event, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_event);

        ((Home) getActivity()).showBackButton(true);

        init(v);
        fillForm();

        v.findViewById(R.id.btAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAudioPopUp();
            }
        });

        v.findViewById(R.id.btFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoPopUp();
            }
        });

        v.findViewById(R.id.btVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVideoPopUp();
            }
        });

        return v;
    }

    private void showPhotoPopUp() {
        final Dialog dialog = new Dialog(getContext(), R.style.DialogNoActionBarMinWidth);
        dialog.setContentView(R.layout.popup_photo_list);
        dialog.setCancelable(true);

        File directory = Environment.getExternalStorageDirectory();
        imagePath = directory + "/ParanaLeiloes/" + item.getId() + "/Fotos";

        File file = new File(imagePath);

        if (!file.exists()) {
            file.mkdirs();
        }

        final ImageView btPhoto = dialog.findViewById(R.id.btPhoto);

        listImages = dialog.findViewById(R.id.listView);

        GridLayoutManager llm = new GridLayoutManager(getActivity(), 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listImages.setLayoutManager(llm);

        btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    try {
                        String image = imagePath + "/IMG_" + photoIndex + ".jpg";

                        File img = new File(image);

                        if (!img.exists())
                            img.createNewFile();

                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                getActivity().getApplicationContext().getPackageName() + ".provider", img);

                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }catch (Exception ex) {
                        System.out.println("");
                    }
                }
            }
        });

        reloadFoto(imagePath, listImages);

        dialog.show();
    }

    private void showAudioPopUp() {
        final Dialog dialog = new Dialog(getContext(), R.style.DialogNoActionBarMinWidth);
        dialog.setContentView(R.layout.popup_audio_list);
        dialog.setCancelable(true);

        File directory = Environment.getExternalStorageDirectory();
        String rootPath = directory + "/ParanaLeiloes/" + item.getId() + "/Audio";

        final MediaRecorder recorder = new MediaRecorder();

        File file = new File(rootPath);

        if (!file.exists()) {
            file.mkdirs();
        }

        final RecyclerView listView = dialog.findViewById(R.id.listView);
        final ImageView mcPlay = dialog.findViewById(R.id.mcPlay);
        final ImageView mcStop = dialog.findViewById(R.id.mcStop);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);

        mcStop.setVisibility(View.GONE);

        mcPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = rootPath + "/Audio_" + UUID.randomUUID() + ".3gp";

                try {

                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setOutputFile(fileName);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                    recorder.prepare();

                    recorder.start();
                } catch (IOException e) {
                }

                mcPlay.setVisibility(View.GONE);
                mcStop.setVisibility(View.VISIBLE);
            }
        });

        mcStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.stop();
                recorder.release();

                mcPlay.setVisibility(View.VISIBLE);
                mcStop.setVisibility(View.GONE);

                reloadAudio(rootPath, listView);
            }
        });

        reloadAudio(rootPath, listView);

        dialog.show();
    }

    private void showVideoPopUp() {
        final Dialog dialog = new Dialog(getContext(), R.style.DialogNoActionBarMinWidth);
        dialog.setContentView(R.layout.popup_video_list);
        dialog.setCancelable(true);

        File directory = Environment.getExternalStorageDirectory();
        videoPath = directory + "/ParanaLeiloes/" + item.getId() + "/Videos";

        File file = new File(videoPath);

        if (!file.exists()) {
            file.mkdirs();
        }

        final ImageView btVideo = dialog.findViewById(R.id.btVideo);

        listVideos = dialog.findViewById(R.id.listView);

        GridLayoutManager llm = new GridLayoutManager(getActivity(), 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listVideos.setLayoutManager(llm);

        btVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    try {
                        String image = videoPath + "/VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").
                                format(new Date()) + ".mp4";

                        File img = new File(image);

                        if (!img.exists())
                            img.createNewFile();

                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                getActivity().getApplicationContext().getPackageName() + ".provider", img);

                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                    }catch (Exception ex) {
                        System.out.println("");
                    }
                }
            }
        });

        reloadVideo(videoPath, listVideos);

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            reloadFoto(imagePath, listImages);
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            reloadVideo(videoPath, listVideos);
        }
    }

    private void reloadAudio(String path, RecyclerView listView) {
        List<String> itens = new ArrayList<String>();

        File list[] = new File(path).listFiles();

        for (int i = 0; i < list.length; i++) {
            String item = list[i].getName();

            if (item.endsWith(".3gp"))
                itens.add(item);
        }

        item.setAudioEvidence(itens);

        AudioAdapter adapter = new AudioAdapter(path, itens);
        listView.setAdapter(adapter);
    }

    private void reloadVideo(String path, RecyclerView listView) {
        List<String> itens = new ArrayList<String>();

        File list[] = new File(path).listFiles();

        for (int i = 0; i < list.length; i++) {
            String item = list[i].getName();

            if (item.endsWith(".mp4"))
                itens.add(item);
        }

        item.setVideoEvidence(itens);

        VideoAdapter adapter = new VideoAdapter(getActivity(), path, itens);
        listView.setAdapter(adapter);
    }

    private void reloadFoto(String path, RecyclerView listView) {
        List<String> itens = new ArrayList<String>();

        File list[] = new File(path).listFiles();

        for (int i = 0; i < list.length; i++) {
            String item = list[i].getName();

            if (item.endsWith(".jpg"))
                itens.add(item);
        }

        photoIndex = itens.size() + 1;

        item.setPhotoEvidence(itens);

        PhotoAdapter adapter = new PhotoAdapter(path, itens);
        listView.setAdapter(adapter);
    }

    private void fillForm() {
        ipDescricao.setText(item.getDescription() != null ? item.getDescription() : "");
        ipMotivoRecolhimento.setText(item.getReason() != null ? item.getReason() : "");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rbTipoOcorrencia.setLayoutManager(llm);

        spTipoOcorrencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String value = (String) adapterView.getItemAtPosition(i);

                    if (item.getOccurrenceTypes() == null) {
                        item.setOccurrenceTypes(new ArrayList<String>());
                    }

                    item.getOccurrenceTypes().add(value);

                    reloadTipoOcorrencia();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ocorrencias = new ArrayList<String>();

        getFirebase().child("occurrence_types").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    ocorrencias.add(d.getValue(String.class));
                }

                reloadTipoOcorrencia();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeItem(int index) {
        if (item.getOccurrenceTypes() == null) {
            item.setOccurrenceTypes(new ArrayList<String>());
        }

        item.getOccurrenceTypes().remove(index);

        reloadTipoOcorrencia();
    }

    private void reloadTipoOcorrencia() {
        List<String> result = new ArrayList<String>();
        result.add("[ -- selecione -- ]");

        if (ocorrencias != null) {
            for (String s : ocorrencias) {
                if (item.getOccurrenceTypes() == null ||
                        (item.getOccurrenceTypes() != null && !item.getOccurrenceTypes().contains(s))) {
                    result.add(s);
                }
            }
        }

        if (item.getOccurrenceTypes() == null) {
            item.setOccurrenceTypes(new ArrayList<String>());
        }

        if (result != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, result);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTipoOcorrencia.setAdapter(adapter);
            spTipoOcorrencia.setSelection(0);

            TipoOcorrenciaAdapter tAdapter = new TipoOcorrenciaAdapter(this, item.getOccurrenceTypes());
            rbTipoOcorrencia.setAdapter(tAdapter);
        }
    }

    private void init(View v) {
        spTipoOcorrencia = v.findViewById(R.id.spTipoOcorrencia);
        rbTipoOcorrencia = v.findViewById(R.id.rbTipoOcorrencia);
        ipDescricao = v.findViewById(R.id.ipDescricao);
        ipMotivoRecolhimento = v.findViewById(R.id.ipMotivoRecolhimento);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceOrder.class.getSimpleName(), item);
        bundle.putInt(BaseFragment.INDEX, position);

        DriverID fragment = new DriverID();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new CarID()).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.menu_next_item, menu);

            View view = Home.toolbar.getMenu().findItem(R.id.nextItem).getActionView();

            view.findViewById(R.id.nextItemBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    save();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    save();
                }
            });
        }
    }

    private void save() {
        if (isValid()) {
            item.setReason(ipMotivoRecolhimento.getText().toString());
            item.setDescription(ipDescricao.getText().toString());

            final String uid = Objects.requireNonNull(getAuth().getCurrentUser()).getUid();
            Query query = getFirebase().child("service_orders").child(uid);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long index = dataSnapshot.getChildrenCount();

                    if (position != null && position != -1)
                        index = position;

                    getFirebase().child("service_orders").child(uid).
                            child(String.valueOf(index)).setValue(item);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ServiceOrder.class.getName(), item);

                    CarInspection fragment = new CarInspection();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("DadoUsuario", databaseError.getMessage().toString());
                }
            });
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    "Há campos a serem preenchidos", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
        boolean bTipoOcorrencia = false;
        boolean bDescricao = false;
        boolean bMotivo = false;

        if (item.getOccurrenceTypes() != null && item.getOccurrenceTypes().size() > 0) {
            bTipoOcorrencia = true;
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    "Deve ser informado ao menos um Tipo de Ocorrência!", Snackbar.LENGTH_SHORT).show();

            bTipoOcorrencia = false;
        }

        if (ipDescricao.getText() != null && ipDescricao.getText().toString().length() > 0) {
            bDescricao = true;
        } else {
            ipDescricao.setError("Campo obrigatório!");
            bDescricao = false;
        }

        if (ipMotivoRecolhimento.getText() != null && ipMotivoRecolhimento.getText().toString().length() > 0) {
            bMotivo = true;
        } else {
            ipMotivoRecolhimento.setError("Campo obrigatório!");
            bMotivo = false;
        }

        return bTipoOcorrencia && bDescricao && bMotivo;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
