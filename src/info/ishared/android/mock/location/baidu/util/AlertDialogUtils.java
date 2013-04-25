package info.ishared.android.mock.location.baidu.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import info.ishared.android.mock.location.baidu.R;

/**
 * User: Lee
 * Date: 11-9-19
 * Time: 下午2:37
 */
public class AlertDialogUtils {
    public interface Executor {
        void execute();
    }
    public interface CallBack{
        void execute(Object... obj);
    }
    /**
     * @param context
     * @param message
     * @param executor
     * @return
     */
    public static void showYesNoDiaLog(Context context, String message, final Executor executor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                executor.execute();
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }

    public static void showConfirmDiaLog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }

    public static void showConfirmDiaLog(Context context, String message,final Executor executor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                executor.execute();
            }
        }).show();
    }

    public static void showInputDialog(Context context, String location, final CallBack executor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_input_layout, null);
        ((EditText)layout.findViewById(R.id.dialog_input_number)).setEnabled(false);
        ((EditText)layout.findViewById(R.id.dialog_input_number)).setText(location);
        builder.setView(layout).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String inputName=((EditText)layout.findViewById(R.id.dialog_input_name)).getText().toString();
                executor.execute(inputName);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
