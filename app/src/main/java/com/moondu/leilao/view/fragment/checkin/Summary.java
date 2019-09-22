package com.moondu.leilao.view.fragment.checkin;

import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.moondu.leilao.R;
import com.moondu.leilao.model.entity.ServiceOrder;
import com.moondu.leilao.view.activity.Home;
import com.moondu.leilao.view.fragment.Tracking;
import com.moondu.leilao.view.fragment.base.BaseFragment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Summary extends BaseFragment {

    private ServiceOrder item;
    private Integer position;
    private Boolean view;

    private Button btnImprimir;
    private Button btnFinalizar;
    private Button btnTracking;

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

    private WebView mWebView;

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
        View v = inflater.inflate(R.layout.summary, container, false);

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
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new ServiceOrders()).addToBackStack(null).commit();
            }
        });

        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Tracking.class);
                startActivity(i);
            }
        });

        btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }

    private void print() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(getActivity());

        // PrintDocumentAdapter printAdapter = new ViewPrintAdapter(this, webView);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("TESTE", "page finished loading " + url);

                createWebPrintJob(view);

                mWebView = null;
            }
        });

        File directory = Environment.getExternalStorageDirectory();
        String path = directory + "/ParanaLeiloes/" + item.getId();
        String imagePath = path + "/Fotos";
        String sigPath = path + "/Signature";

        // Generate an HTML document on the fly:
        String part1 = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<title>Paraná Leilões</title>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "<style>\n" +
                "/* Style the body */\n" +
                "body {\n" +
                "  font-family: Arial;\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                "/* Header/Logo Title */\n" +
                ".header {\n" +
                "  padding: 8px;\n" +
                "  text-align: left;\n" +
                "  background: #1abc9c;\n" +
                "  color: white;\n" +
                "  font-size: 15px;\n" +
                "}\n" +
                "\n" +
                "/* Page Content */\n" +
                ".content {padding:30px;}\n" +
                "\n" +
                ".bottom {padding:30px;\n" +
                "text-align: right;}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "  <h1>Paraná Leilões</h1>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "<div class=\"header\">\n" +
                "  <h2>Veículo</h2>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "<b>Placa: </b>" + item.getIdentification() + "</br>\n" +
                "<b>Endereço: </b>" + item.getIdType() + "</br>\n" +
                "<b>Tipo: </b>" + item.getLocation() + "</br>\n" +
                "<b>Modelo: </b>" + item.getModel() + "</br>\n" +
                "<b>Cor: </b>" + item.getColor() + "</br>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"header\">\n" +
                "  <h2>Condutor</h2>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "<b>Nome: </b>" + item.getDriver() + "</br>\n" +
                "<b>RG: </b>" + item.getRg() + "</br>\n" +
                "<b>CPF: </b>" + item.getCpf() + "</br>\n" +
                "<b>CNH: </b>" + item.getCnh() + "</br>\n" +
                "<b>Endereço: </b>" + item.getDriverAddress() + "</br>\n" +
                "<b>E-mail: </b>" + item.getMail() + "</br>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"header\">\n" +
                "  <h2>Ocorrências</h2>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "<b>Tipo de ocorrência: </b>" + item.getIdentification() + "</br>\n" +
                "<b>Descrição: </b>" + item.getDescription() + "</br>\n" +
                "  <table style=\"width:100%\"> \n" +
                "  <tr>\n" +
                "    <th>Evidências:</th>\n" +
                "    <th>  </th>\n" +
                "    <th> </th>\n" +
                "  </tr>\n";
        String part2 =
                "  <tr>\n" +
                        "    <td> <img src=\"file://" + imagePath + "/IMG_1.jpg\" height=\"150\" width=\"300\"> </td>\n" +
                        "    <td> <img src=\"file://" + imagePath + "/IMG_2.jpg\" height=\"150\" width=\"300\"> </td>\n" +
                        "    <td> <img src=\"file://" + imagePath + "/IMG_3.jpg\" height=\"150\" width=\"300\"> </td>\n" +
                        "    <td> <img src=\"file://" + imagePath + "/IMG_4.jpg\" height=\"150\" width=\"300\"> </td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "  </tr>\n" +
                        "</table> </br> </br>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"header\">\n" +
                "  <h2>Detalhes de Vistoria</h2>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "<b>Veículo removido para Base do Prestador? </b>" + item.getRemovedToBase() + "</br>\n" +
                "<b>Vistoria realizada em local escuro? </b>" + item.getDarkPlace() + "</br>\n" +
                "<b>Cliente acompanhou o transporte? </b>" + item.getIdentification() + "</br>\n" +
                "<b>Reboquista teve acesso ao interior do veículo? </b>" + item.getIdentification() + "</br>\n" +
                "<b>Cliente retirou os pertences pessoais? </b>" + item.getRemovedBelongings() + "</br>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"header\">\n" +
                "  <h2>Danos e Avarias no Veículo</h2>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "Não há avarias</b></br></br>\n" +
                "\n" +
                "<b>Pneu 1 - Estado: </b>" + item.getTireCondition1() + "- <b>Marca: </b>" + item.getTireBrand1() + "</br>\n" +
                "<b>Pneu 2 - Estado: </b>" + item.getTireCondition2() + "- <b>Marca: </b>" + item.getTireBrand2() + "</br>\n" +
                "<b>Pneu 3 - Estado: </b>" + item.getTireCondition3() + "- <b>Marca: </b>" + item.getTireBrand3() + "</br>\n" +
                "<b>Pneu 4 - Estado: </b>" + item.getTireCondition4() + "- <b>Marca: </b>" + item.getTireBrand4() + "</br>\n" +
                "<b>Estepe - Estado: </b>" + item.getTireCondition5() + "- <b>Marca: </b>" + item.getTireBrand5() + "</br>\n" +
                "<b>Estado de tanque: <b>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"header\">\n" +
                "  <h2>Acessórios e Equipamentos</h2>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"content\">\n" +
                "\n" +
                "<table style=\"width:100%\"> \n" +
                "  <tr>\n" +
                "    <th> </th>\n" +
                "    <th> </th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "<td> <b>CRLV - </b>>" + item.getAccessory1() + "</br>\n" +
                "<b>Chave Principal - </b>" + item.getAccessory2() + "</br>\n" +
                "<b>Chave Reserva - </b>" + item.getAccessory3() + "</br>\n" +
                "<b>Manual - </b>" + item.getAccessory4() + "</br>\n" +
                "<b>Bateria - </b>" + item.getAccessory5() + "</br>\n" +
                "<b>CD/DVD - </b>" + item.getAccessory6() + "</br>\n" +
                "<b>Extintor - </b>" + item.getAccessory7() + "</br>\n" +
                "<b>Macaco - </b" + item.getAccessory8() + "</br>\n" +
                "<b>Triângulo - </b>" + item.getAccessory9() + "</br>\n" +
                "<b>Chave de Roda - </b>" + item.getAccessory10() + "</br>\n" +
                "<b>Alarme - </b>" + item.getAccessory11() + "</br>\n" +
                "<b>Faróis de Neblina -</b>" + item.getAccessory12() + "</br>\n" +
                "<b>Retrovisores - </b>" + item.getAccessory13() + "</br>\n" +
                "\n" +
                "    <td><b>Antena - </b>" + item.getAccessory14() + "</br>\n" +
                "<b>Alto Falantes - </b>" + item.getAccessory15() + "</br>\n" +
                "<b>Tapetes - </b>" + item.getAccessory16() + "</br>\n" +
                "<b>Santo Antônio - </b>" + item.getAccessory17() + "</br>\n" +
                "<b>Engate - </b>" + item.getAccessory18() + "</br>\n" +
                "<b>Módulo - </b>" + item.getAccessory19() + "</br>\n" +
                "<b>Quebra Mato - </b>" + item.getAccessory20() + "</br>\n" +
                "<b>Painel - </b>" + item.getAccessory21() + "</br>\n" +
                "<b>Tampas Laterais - </b>" + item.getAccessory22() + "</br>\n" +
                "<b>Para lama Dianteiro - </b>" + item.getAccessory23() + "</br>\n" +
                "<b>Para lama Traseiro - </b><" + item.getAccessory24() + "</br>\n" +
                "<b>Escapamento - </b>" + item.getAccessory25() + "</br>\n" +
                "<b>Placa - </b>" + item.getAccessory26() + "</br>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "  </tr>\n" +
                "</table> </br> </br>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<table style=\"width:100%\"> \n" +
                "  <tr>\n" +
                "    <th>Assinatura Motorista:</th>\n" +
                "    <th>Assinatura Operador:  </th>\n" +
                "    <th>Assinatura Oficial: </th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td> <img src=\"file://" + sigPath + "/Signature_Driver.jpg\" height=\"150\" width=\"300\"> </td>\n" +
                "    <td> <img src=\"file://" + sigPath + "/Signature_Official.jpg\" height=\"150\" width=\"300\"> </td>\n" +
                "    <td> <img src=\"file://" + sigPath + "/Signature_Operator.jpg\" height=\"150\" width=\"300\"> </td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "  </tr>\n" +
                "</table> </br> </br>\n" +
                "<div class=\"bottom\">\n" +
                "Curitiba 12 de Setembro de 2019, 17:45\n" +
                "</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        String images = "<tr>";

        Log.d("teste", sigPath);

        File dirImage = new File(imagePath);


        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.loadDataWithBaseURL(null, part1 + part2, "text/HTML",
                "UTF-8", null);

        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {
        PrintManager printManager = (PrintManager) getActivity()
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    private void init(View v) {
        llImages = v.findViewById(R.id.llImages);

        btnImprimir = v.findViewById(R.id.btnPrint);
        btnFinalizar = v.findViewById(R.id.btnFinish);
        btnTracking = v.findViewById(R.id.btnTracking);

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
