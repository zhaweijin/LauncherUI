package com.mirageTeam.common;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class AnimationUtils {

	public static Animation ScaleAnimationZoom() {
		ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(400);
		return animation;

	}
	
	public static Animation ScaleAnimationOut() {
		ScaleAnimation animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(400);
		return animation;

	}
}
