package com.moondu.leilao.view.others;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.print.PrintHelper;

import java.util.ArrayList;
import java.util.List;

public class Print {

    private static List<PrintJob> mPrintJobs = new ArrayList<PrintJob>();

    public static void printImage(Activity activity, String filePath) {

        PrintHelper photoPrinter = new PrintHelper(activity);

        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.setColorMode(PrintHelper.COLOR_MODE_MONOCHROME);

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droids);

        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }

    public static void printHtml(final Activity activity) {

        WebView webView = new WebView(activity);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Log.i(TAG, "page finished loading " + url);
                createWebPrintJob(activity, view);
            }
        });



        String htmlDocument = getHTMLTemplate();

        webView.loadDataWithBaseURL("file:///android_res/drawable/", htmlDocument, "text/HTML", "UTF-8", null);
    }

    private static String getHTMLTemplate(){

        String name = "Vitor";
        String company = "Moondu Soluções";
        String year = "2019";

        String htmlDocument = "";

        htmlDocument += "<html lang='en' xmlns='http://www.w3.org/1999/xhtml'><head><meta charset='utf-8' />";
        htmlDocument += "<meta name='viewport' content='width=device-width, initial-scale=1'>";
        htmlDocument += "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css'>";
        htmlDocument += "<title>Orçamento Aprovado</title>";
        htmlDocument += "<style>.card-footer {color: #A9A9A9;text-align: center;} a {color: #A9A9A9 !important;text-decoration: none;} .links {padding: 1px;background: linear-gradient(to right, #08414e, #08414e);} .card-header {padding: .75rem 1.25rem;margin-bottom: 0;background: linear-gradient(to right, rgba(0,0,0,.03), rgba(0,0,0,.03));border-bottom: 1px solid rgba(0,0,0,.125);} </style>";
        htmlDocument += "</head><body>";
        htmlDocument += "<div class='card'><div class='card-header'>";
        htmlDocument += "<img src='comany_logo.png' style='max-width: 160px' />";
        htmlDocument += "</div><div style='padding:20px 50px; '><p>";
        htmlDocument += "Olá "+ name  +",</p><br /> <br /><p>";
        htmlDocument += "O orçamento de José, versão 1.0 referente ao ano "+ year +" foi aprovado.";
        htmlDocument += "</p><br /> <br /><p>";
        htmlDocument += "Para mais informações acesse <b>Sistema de Orçamentos</b>.</p><br /> <br />";
        htmlDocument += "<p>Atenciosamente, </p><p>"+ company +"</p></div>";
        htmlDocument += "<div class='card-footer links'><p>";
        htmlDocument += "<a href='https://google.com/' target='_blank'>"+ company +"</a> © "+ year +" |";
        htmlDocument += "<a href='https://google.com/terms-of-use/' target='_blank'>Termos de Uso</a> |";
        htmlDocument += "<a href='https://google.com/contact/' target='_blank'>Contate-nos</a>";
        htmlDocument += "</p></div></div></body></html>";

        return htmlDocument;
    }

    private static void createWebPrintJob(Activity activity, WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);

        String jobName = "App Html Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        if(printManager != null){

            PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());

            // Save the job object for later status checking
            mPrintJobs.add(printJob);

            if(printJob.isCompleted()){

                String msg = "success";
            }else{

                String msg = "fail";
            }
        }
    }
}
