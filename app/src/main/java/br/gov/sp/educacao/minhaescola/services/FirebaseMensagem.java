package br.gov.sp.educacao.minhaescola.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import br.gov.sp.educacao.minhaescola.R;

public class FirebaseMensagem
        extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> map = new HashMap<>();

        map = remoteMessage.getData();

        String modulo = map.get("Modulo");

        if (modulo != null) {

            switch (modulo) {

                case "carteirinha":

                    NotificationManager nm = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                    builder.setContentTitle("A Carteirinha do Aluno está disponível!");
                    builder.setContentText("A foto da carteirinha foi aprovada");
                    builder.setColor(getResources().getColor(R.color.azul8));
                    builder.setSmallIcon(R.drawable.ic_notificacao_carteitinha);
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        String channelId = getApplicationContext().getString(R.string.default_notification_channel_id);

                        NotificationChannel channel = new NotificationChannel(channelId, "Foto da Carteirinha", NotificationManager.IMPORTANCE_DEFAULT);

                        channel.setDescription("Aprovado");

                        nm.createNotificationChannel(channel);

                        builder.setChannelId(channelId);
                    }
                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        builder.setContentTitle("Minha Escola SP");

                        builder.setContentText("Sua foto da carteirinha foi Aprovado");
                    }

                    Notification n = builder.build();

                    nm.notify(R.drawable.ic_notificacao_carteitinha, n);
                    break;

                case "boletim":

                    NotificationManager nms = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builders = new NotificationCompat.Builder(getApplicationContext());

                    builders.setContentTitle("Boletim já disponivel");
                    builders.setContentText("Novo Boletim Adicionado");
                    builders.setColor(getResources().getColor(R.color.azul8));
                    builders.setSmallIcon(R.drawable.ic_notificacao_boletim);
                    builders.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        String channelId = getApplicationContext().getString(R.string.default_notification_channel_id);
                        NotificationChannel channel = new NotificationChannel(channelId, "Boletim", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.setDescription("Seu Boletim esta pronto");
                        nms.createNotificationChannel(channel);
                        builders.setChannelId(channelId);

                    }
                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        builders.setContentTitle("Minha Escola SP");
                        builders.setContentText("Boletim Já Disponivel");
                    }

                    Notification nn = builders.build();
                    nms.notify(R.drawable.ic_notificacao_boletim, nn);
                    break;
            }
        }
    }
}