package com.moondu.leilao.view.fragment.checkout;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moondu.leilao.R;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.base.BaseFragment;
import com.moondu.leilao.view.fragment.checkin.CarEvent;
import com.moondu.leilao.view.fragment.checkin.DriverID;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.moondu.leilao.model.firebase.FirebaseHelper.getAuth;
import static com.moondu.leilao.model.firebase.FirebaseHelper.getFirebase;

public class Summary extends BaseFragment {

    private ServiceOrder item;
    private Integer position;
    private Boolean view;

    private Button btnFinalizar;

    private EditText ipDescricao;

    private TextView cOs;

    private TextView cPlaca;
    private TextView cEndereco;
    private TextView cTipo;
    private TextView cModelo;
    private TextView cColor;

    private TextView dNome;
    private TextView dRg;
    private TextView dCpf;
    private TextView dCnh;
    private TextView dAddress;
    private TextView dEmail;
    private TextView dTelefone;

    private TextView eTipoOcorrencia;
    private TextView eDescription;

    private TextView vInfo1;
    private TextView vInfo2;
    private TextView vInfo3;
    private TextView vInfo4;
    private TextView vInfo5;

    private TextView ipInfo1;
    private TextView ipInfo2;
    private TextView ipInfo3;
    private TextView ipInfo4;
    private TextView ipInfo5;
    private TextView ipInfo6;
    private TextView ipInfo7;
    private TextView ipInfo8;
    private TextView ipInfo9;
    private TextView ipInfo10;
    private TextView ipInfo11;
    private TextView ipInfo12;
    private TextView ipInfo13;
    private TextView ipInfo14;
    private TextView ipInfo15;
    private TextView ipInfo16;
    private TextView ipInfo17;
    private TextView ipInfo18;
    private TextView ipInfo19;
    private TextView ipInfo20;
    private TextView ipInfo21;
    private TextView ipInfo22;
    private TextView ipInfo23;
    private TextView ipInfo24;
    private TextView ipInfo25;
    private TextView ipInfo26;
    private TextView ipInfo27;

    private TextView cdAvarias;
    private TextView cdInfo1;
    private TextView cdInfo2;
    private TextView cdInfo3;
    private TextView cdInfo4;
    private TextView cdInfo5;
    private TextView cdInfo6;

    private ImageView signProprietario;
    private ImageView signCondutor;
    private ImageView signOficial;

    private LinearLayout llImages;

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
        View v = inflater.inflate(R.layout.summary_checkout, container, false);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.menu_summary);

        ((Home) getActivity()).showBackButton(false);

        init(v);
        fillForm();

        return v;
    }

    private void fillForm() {
        String tipo = item.getIdType().equals(1) ? "Placa" :
                (item.getIdType().equals(1) ? "Chassi" : "Outro");
        String ocorrencia = "";
        String avaria = "";

        Map<String, String> pne = new HashMap<String, String>();
        pne.put("B", "Bom");
        pne.put("M", "Médio");
        pne.put("R", "Ruim");

        Map<String, String> tpne = new HashMap<String, String>();
        tpne.put("C", "Calota");
        tpne.put("L", "Liga Leve");
        tpne.put("I", "Inexistente");

        Map<String, String> acc = new HashMap<String, String>();
        acc.put("S", "Sim");
        acc.put("N", "Não");
        acc.put("I", "Inexistente/Avariado");

        Map<Integer, String> vst = new HashMap<Integer, String>();
        vst.put(1, "Sim");
        vst.put(2, "Não");
        vst.put(3, "N/A");

        Map<Integer, String> fue = new HashMap<Integer, String>();
        fue.put(1, "Tanque Cheio");
        fue.put(2, "3/4");
        fue.put(3, "1/2");
        fue.put(4, "1/4");

        if (item.getOccurrenceTypes() != null) {
            for (String s : item.getOccurrenceTypes()) {
                ocorrencia += s + ",";
            }

            if (ocorrencia.length() > 0)
                ocorrencia = ocorrencia.substring(0, ocorrencia.length() - 1);
        }

        if (item.getDamage() != null) {
            for (String s : item.getDamage()) {
                avaria += s + ",";
            }

            if (avaria.length() > 0)
                avaria = avaria.substring(0, avaria.length() - 1);
        }

        cOs.setText(item.getId());

        ipDescricao.setText(item.getCheoutInfo());

        cPlaca.setText(tipo + ": " + item.getIdentification());
        cEndereco.setText("Localização:" + item.getLocation());
        cTipo.setText("Tipo:" + item.getVehicleType());
        cModelo.setText("Modelo:" + item.getModel());
        cColor.setText("Cor:" + item.getColor());
        dNome.setText("Nome:" + item.getDriver());
        dRg.setText("RG:" + item.getRg());
        dCpf.setText("CPF:" + item.getCpf());
        dCnh.setText("CNH:" + item.getCnh());
        dAddress.setText("Endereço:" + item.getDriverAddress());
        dEmail.setText("E-mail:" + item.getMail());
        dTelefone.setText("Telefone:" + item.getPhone());
        eTipoOcorrencia.setText("Tipo de Ocorrência:" + ocorrencia);
        eDescription.setText("Descrição:" + item.getDescription());

        vInfo1.setText("Veículo Removido para a Base do Prestador? " + vst.get(item.getRemovedToBase()));
        vInfo2.setText("Vistoria realizada em local escuro? " + vst.get(item.getDarkPlace()));
        vInfo3.setText("Cliente acompanhou o transporte? " + vst.get(item.getTrackShipping()));
        vInfo4.setText("Reboquista teve acesso ao interior do veículo? " + vst.get(item.getInteriorVehicle()));
        vInfo5.setText("Cliente retirou pertences pessoais? " + vst.get(item.getRemovedBelongings()));

        ipInfo1.setText("CRLV: " + acc.get(item.getAccessory1()));
        ipInfo2.setText("Chave Principal: " + acc.get(item.getAccessory2()));
        ipInfo3.setText("Chave Reserva: " + acc.get(item.getAccessory3()));
        ipInfo4.setText("Manual: " + acc.get(item.getAccessory4()));
        ipInfo5.setText("Bateria: " + acc.get(item.getAccessory5()));
        ipInfo6.setText("CD/DVD: " + acc.get(item.getAccessory6()));
        ipInfo7.setText("Extintor: " + acc.get(item.getAccessory7()));
        ipInfo8.setText("Macaco: " + acc.get(item.getAccessory8()));
        ipInfo9.setText("Triângulo: " + acc.get(item.getAccessory9()));
        ipInfo10.setText("Chave de Roda: " + acc.get(item.getAccessory10()));
        ipInfo11.setText("Alarme: " + acc.get(item.getAccessory11()));
        ipInfo12.setText("Faróis de Neblina: " + acc.get(item.getAccessory12()));
        ipInfo13.setText("Retrovisores: " + acc.get(item.getAccessory13()));
        ipInfo14.setText("Antena: " + acc.get(item.getAccessory14()));
        ipInfo15.setText("Alto Falantes: " + acc.get(item.getAccessory15()));
        ipInfo16.setText("Tapetes: " + acc.get(item.getAccessory16()));
        ipInfo17.setText("Santo Antônio: " + acc.get(item.getAccessory17()));
        ipInfo18.setText("Engate: " + acc.get(item.getAccessory18()));
        ipInfo19.setText("Módulo: " + acc.get(item.getAccessory19()));
        ipInfo20.setText("Quebra Mato: " + acc.get(item.getAccessory20()));
        ipInfo21.setText("Painel: " + acc.get(item.getAccessory21()));
        ipInfo22.setText("Guidão: " + acc.get(item.getAccessory22()));
        ipInfo23.setText("Tampas Laterais: " + acc.get(item.getAccessory23()));
        ipInfo24.setText("Para Lama Dianteiro: " + acc.get(item.getAccessory24()));
        ipInfo25.setText("Para lama Traseiro: " + acc.get(item.getAccessory25()));
        ipInfo26.setText("Escapamento: " + acc.get(item.getAccessory26()));
        ipInfo27.setText("Placa: " + acc.get(item.getAccessory27()));

        if (avaria != null && avaria.trim().length() > 0) {
            cdAvarias.setText("Avarias: " + avaria);
        } else {
            cdAvarias.setText("Não há avarias.");
        }

        if (item.getTireType1() != null)
            if (item.getTireType1().equals("I")) {
                cdInfo1.setText("Pneu DD: Inexistente");
            } else {
                cdInfo1.setText("Pneu DD: Estado:" + pne.get(item.getTireCondition1()) + " - Marca: " +
                        item.getTireBrand1() + " - Tipo : " + tpne.get(item.getTireType1()));
            }

        if (item.getTireType2() != null)
            if (item.getTireType2().equals("I")) {
                cdInfo2.setText("Pneu TD: Inexistente");
            } else {
                cdInfo2.setText("Pneu TD: Estado:" + pne.get(item.getTireCondition2()) + " - Marca: " +
                        item.getTireBrand2() + " - Tipo : " + tpne.get(item.getTireType2()));
            }

        if (item.getTireType3() != null)
            if (item.getTireType3().equals("I")) {
                cdInfo3.setText("Pneu DE: Inexistente");
            } else {
                cdInfo3.setText("Pneu DE: Estado:" + pne.get(item.getTireCondition3()) + " - Marca: " +
                        item.getTireBrand3() + " - Tipo : " + tpne.get(item.getTireType3()));
            }

        if (item.getTireType4() != null)
            if (item.getTireType4().equals("I")) {
                cdInfo4.setText("Pneu TD: Inexistente");
            } else {
                cdInfo4.setText("Pneu TD: Estado:" + pne.get(item.getTireCondition4()) + " - Marca: " +
                        item.getTireBrand4() + " - Tipo : " + tpne.get(item.getTireType4()));
            }

        if (item.getTireType5() != null)
            if (item.getTireType5().equals("I")) {
                cdInfo5.setText("Estepe: Inexistente");
            } else {
                cdInfo5.setText("Estepe: Estado:" + pne.get(item.getTireCondition5()) + " - Marca: " +
                        item.getTireBrand5() + " - Tipo : " + tpne.get(item.getTireType5()));
            }

        cdInfo6.setText("Combustível: " + fue.get(item.getFuelLevel()));

        File directory = Environment.getExternalStorageDirectory();
        String imagePath = directory + "/ParanaLeiloes/" + item.getId() + "/Fotos";

        File dirImage = new File(imagePath);

        if (dirImage.exists()) {
            File list[] = dirImage.listFiles();

            for (int i = 0; i < list.length; i++) {
                String item = list[i].getName();

                if (item.endsWith(".jpg")) {
                    ImageView iv = new ImageView(getActivity());
                    iv.setImageBitmap(BitmapFactory.decodeFile(imagePath + "/" + item));

                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT, 350);

                    lp.setMargins(5, 5, 5, 5);
                    iv.setPadding(5, 5, 5, 5);

                    iv.setLayoutParams(lp);

                    llImages.addView(iv);
                }
            }
        }

        directory = Environment.getExternalStorageDirectory();
        String rootPath = directory + "/ParanaLeiloes/" + item.getId() + "/Signature";

        File img = new File(rootPath, "Signature_Driver.jpg");

        if (img.exists())
            signProprietario.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));

        img = new File(rootPath, "Signature_Operator.jpg");

        if (img.exists())
            signCondutor.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));

        img = new File(rootPath, "Signature_Official.jpg");

        if (img.exists())
            signOficial.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    item.setCheoutInfo(ipDescricao.getText().toString());

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

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, new CheckOut()).addToBackStack(null).commit();
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
        });
    }

    private boolean isValid() {
        if (ipDescricao.getText() != null && ipDescricao.getText().toString().length() > 0) {
            return true;
        } else {
            ipDescricao.setError("Campo obrigatório!");
            return false;
        }
    }


    private void init(View v) {
        llImages = v.findViewById(R.id.llImages);

        ipDescricao = v.findViewById(R.id.ipDescricao);
        btnFinalizar = v.findViewById(R.id.btnFinish);

        cOs = v.findViewById(R.id.cOs);
        cPlaca = v.findViewById(R.id.cPlaca);
        cEndereco = v.findViewById(R.id.cEndereco);
        cTipo = v.findViewById(R.id.cTipo);
        cModelo = v.findViewById(R.id.cModelo);
        cColor = v.findViewById(R.id.cColor);
        dNome = v.findViewById(R.id.dNome);
        dRg = v.findViewById(R.id.dRg);
        dCpf = v.findViewById(R.id.dCpf);
        dCnh = v.findViewById(R.id.dCnh);
        dAddress = v.findViewById(R.id.dAddress);
        dEmail = v.findViewById(R.id.dEmail);
        dTelefone = v.findViewById(R.id.dTelefone);
        eTipoOcorrencia = v.findViewById(R.id.eTipoOcorrencia);
        eDescription = v.findViewById(R.id.eDescription);

        vInfo1 = v.findViewById(R.id.vInfo1);
        vInfo2 = v.findViewById(R.id.vInfo2);
        vInfo3 = v.findViewById(R.id.vInfo3);
        vInfo4 = v.findViewById(R.id.vInfo4);
        vInfo5 = v.findViewById(R.id.vInfo5);

        ipInfo1 = v.findViewById(R.id.ipInfo1);
        ipInfo2 = v.findViewById(R.id.ipInfo2);
        ipInfo3 = v.findViewById(R.id.ipInfo3);
        ipInfo4 = v.findViewById(R.id.ipInfo4);
        ipInfo5 = v.findViewById(R.id.ipInfo5);
        ipInfo6 = v.findViewById(R.id.ipInfo6);
        ipInfo7 = v.findViewById(R.id.ipInfo7);
        ipInfo8 = v.findViewById(R.id.ipInfo8);
        ipInfo9 = v.findViewById(R.id.ipInfo9);
        ipInfo10 = v.findViewById(R.id.ipInfo10);
        ipInfo11 = v.findViewById(R.id.ipInfo11);
        ipInfo12 = v.findViewById(R.id.ipInfo12);
        ipInfo13 = v.findViewById(R.id.ipInfo13);
        ipInfo14 = v.findViewById(R.id.ipInfo14);
        ipInfo15 = v.findViewById(R.id.ipInfo15);
        ipInfo16 = v.findViewById(R.id.ipInfo16);
        ipInfo17 = v.findViewById(R.id.ipInfo17);
        ipInfo18 = v.findViewById(R.id.ipInfo18);
        ipInfo19 = v.findViewById(R.id.ipInfo19);
        ipInfo20 = v.findViewById(R.id.ipInfo20);
        ipInfo21 = v.findViewById(R.id.ipInfo21);
        ipInfo22 = v.findViewById(R.id.ipInfo22);
        ipInfo23 = v.findViewById(R.id.ipInfo23);
        ipInfo24 = v.findViewById(R.id.ipInfo24);
        ipInfo25 = v.findViewById(R.id.ipInfo25);
        ipInfo26 = v.findViewById(R.id.ipInfo26);
        ipInfo27 = v.findViewById(R.id.ipInfo27);

        cdAvarias = v.findViewById(R.id.cdAvarias);
        cdInfo1 = v.findViewById(R.id.cdInfo1);
        cdInfo2 = v.findViewById(R.id.cdInfo2);
        cdInfo3 = v.findViewById(R.id.cdInfo3);
        cdInfo4 = v.findViewById(R.id.cdInfo4);
        cdInfo5 = v.findViewById(R.id.cdInfo5);
        cdInfo6 = v.findViewById(R.id.cdInfo6);

        signProprietario = v.findViewById(R.id.dSignImage);
        signCondutor = v.findViewById(R.id.gSignImage);
        signOficial = v.findViewById(R.id.oSignImage);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
