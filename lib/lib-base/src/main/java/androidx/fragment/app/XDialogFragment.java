package androidx.fragment.app;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/23 3:56 PM
 * @description: 修改DialogFragment中的show方法
 * <p>
 * 1、DialogFragment是继承于Fragment，在内部创建了一个Dialog对象
 * 2、在show和dismiss方法中通过对Fragment的add和remove来显示Dialog
 * 3、内部Dialog通过onCreateDialog方法自动创建。Dialog在onActivityCreated方法中setContentView()
 * 4、每次dismiss时，Fragment会一直执行完整个生命周期（onDetach），所以每次show的时候，生命周期会重新走一遍
 * 5、生命周期执行顺序：onAttach-->onCreate-->onCreateDialog-->onCreateView-->onViewCreated-->onActivityCreated-->onStart
 * </p>
 * @since: 1.0.0
 */
public class XDialogFragment extends DialogFragment {

    @Override
    public void show(FragmentManager manager, String tag) {
//        mDismissed = false;
//        mShownByMe = true;
//        if (manager != null) {
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commitAllowingStateLoss();
//        }
    }
}
