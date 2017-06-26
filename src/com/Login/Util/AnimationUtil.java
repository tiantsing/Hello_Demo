/**
 * APP启动引导界面
 * create 2016-07-01 23:15
 */
package com.Login.Util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
/*
 * 本类为动画类
 * 实现引导页的动画
 */
public class AnimationUtil {

	public static LayoutAnimationController getListAnimTranslate() {
		//设置动画组
		AnimationSet set = new AnimationSet(true);
		
		// 设置加载动画透明度渐变从（0.1不显示-1.0完全显示）
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		// 设置动画组动画时间0.5s
		animation.setDuration(500);
		// 将动画组与组件关联
		set.addAnimation(animation);
		
        //动画设置
		/**
		 * 参数1~2：x轴开始位置
		 * 参数3~4：y轴开始位置
		 * 参数5~6：x轴结束位置
		 * 参数7~8：y轴结束位置
		 */
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		// 设置动画时间0.8s
		animation.setDuration(800);
		// 将动画与组件关联
		set.addAnimation(animation);
		//Layout动画控制器
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);

		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		
		return controller;
	}
	
}
