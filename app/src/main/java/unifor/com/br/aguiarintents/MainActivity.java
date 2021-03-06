package unifor.com.br.aguiarintents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button makeVideoButton;
    Button galeryButton;
    Button playVideoButton;
    TextView feedTextView;
    VideoView videoView;

    ImageButton playButton;
    Uri uriCaminho;

    static final int REQUEST_VIDEO_CAPTURE = 1;

    static final int REQUEST_VIDEO_GET = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        makeVideoButton = (Button) findViewById(R.id.fazer_video);
        galeryButton = (Button) findViewById(R.id.ver_galeria);
        playVideoButton = (Button) findViewById(R.id.play_video);

        playButton = (ImageButton) findViewById(R.id.play_video_local);

        videoView = (VideoView) findViewById(R.id.videoView);

        feedTextView = (TextView) findViewById(R.id.feed_video);

        makeVideoButton.setOnClickListener(this);
        galeryButton.setOnClickListener(this);
        playVideoButton.setOnClickListener(this);

        playButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fazer_video:
                Toast.makeText(this, "Novo Video", Toast.LENGTH_SHORT).show();
                callCamera();
                break;
            case R.id.ver_galeria:
                Toast.makeText(this, "Selecionar videos", Toast.LENGTH_SHORT).show();
                getVideo();
                break;
            case R.id.play_video:
                if (uriCaminho != null) {
                    playVideo(uriCaminho);
                } else {
                    feedTextView.setText("Nenhum video selecionado");
                    Toast.makeText(this, "Você ainda não gravou nenhum video.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.play_video_local:

                if (!videoView.isPlaying()) {

                    if (uriCaminho != null) {
                        MediaController mediaController = new
                                MediaController(this);
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                        videoView.setVideoURI(uriCaminho);
                        videoView.start();
                    }
                } else {
                    videoView.pause();
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                }
                break;
        }
    }

    private void callCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void playVideo(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void getVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_VIDEO_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_VIDEO_CAPTURE:
                if (resultCode == RESULT_OK) {
                    this.uriCaminho = data.getData();
                } else {
                    Toast.makeText(this, "Não houve captura do video.", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_VIDEO_GET:
                if (resultCode == RESULT_OK) {
                    this.uriCaminho = data.getData();
                } else {
                    Toast.makeText(this, "Nenhum video selecionado.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        if (uriCaminho != null) {
            feedTextView.setText(uriCaminho.getPath());
        } else {
            feedTextView.setText("Nenhum video carregado !");
        }
    }
}
