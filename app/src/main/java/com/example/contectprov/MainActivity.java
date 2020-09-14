package com.example.contectprov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Define variables
    Button boton;
    Button btnSave;
    TextView texto1;
    EditText eTxtPhone;
    EditText eTxtName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se establece relación entre la parte gráfica y se obtiene los datos ingresados
        boton = (Button) findViewById(R.id.boton);
        btnSave = (Button) findViewById(R.id.btnSave);
        texto1 = (TextView) findViewById(R.id.texto);
        //Se espera la accion de usuario
        boton.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        this.eTxtName = (EditText) findViewById(R.id.editTextTextPersonName);
        this.eTxtPhone = (EditText) findViewById(R.id.editTextPhone);
    }

    //Captura acción de usuario y determina la acción a ejecutar
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boton:
                ObtenerDatos();
                break;
            case R.id.btnSave:
                Log.d("MESSAGE","Evento Guardar");
                insertData();
                break;
            default:
                break;
        }

    }

    public void insertData(){
        //Obtenemos Los valores de los input y guardamos en la base de datos 
        String DisplayName = eTxtName.getText().toString();
        String MobileNumber = eTxtPhone.getText().toString();
        String HomeNumber = "";
        String WorkNumber = "";
        String emailID = "";
        String company = "";
        String jobTitle = "";

        //Obtenemos Los valores de los input
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if(DisplayName != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName).build());
        }
        //------------------------------------------------------ Mobile Number
        if(MobileNumber != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if(HomeNumber != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if(WorkNumber != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if(emailID != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if(!company.equals("") && !jobTitle.equals(""))
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try
        {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Genera
    public void ObtenerDatos() {
        //en una variable de tipo string projec Se define los campor a seleccionar de las tabla: ContactsContract.Data, CLASE ContactsContract.CommonDataKinds con la cual se obtiene
        //el número de telefono y el tipo
        String[] projeccion = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE};
        //Clausula de selección
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        //Ordenar
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        //Obtener los datos de cursor que llama a un ContentResolves que obtiene los datos
        Cursor c = getContentResolver().query(         // ContactResolves al cual al que pasarle Usr, projeccion,selectionClause,null,sortOrder)
                ContactsContract.Data.CONTENT_URI,     //URI ES UNA CADENA DE CONEXION A CUALQUIER TIPO DE FICHERO
                projeccion,                           //Datos a obtener las columnas
                selectionClause,                     //Criterios de selección
                null,                   //Si se usa el signo ?  se colocn la lista de parametros, si se coloca el camp se coloca null
                sortOrder);                          //El orden en el cual se desea recuperar los dator

        texto1.setText(""); //Se limpia rl texto
        //Recorre el cursor y obtiene los datos de los contactos del telefono
        while (c.moveToNext()) {
            texto1.append("Identificador: " + c.getString(0) + ". Nombre: " + c.getString(1) + " Numero: " + c.getString(2) + " Tipo: " + c.getString(3) + "\n \n");
        }

        c.close();
    }
}
