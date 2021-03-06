package com.sysu.pro.fade.home.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sysu.pro.fade.R;
import com.sysu.pro.fade.home.beans.ContentBean;
import com.sysu.pro.fade.home.beans.RelayBean;

import java.util.List;

/**
 * Created by LaiXiancheng on 2017/8/2.
 * 信息流中item的ViewHolder的基类，实现了三种（仅图片，仅文字，图片加文字）item的基础（共同）功能
 */

abstract public class HomeBaseViewHolder extends RecyclerView.ViewHolder {
	TextView tvName, tvBody;    //name为用户名，body为正文
	ImageButton commentButton;    //评论按键
	ImageButton addTimeButton;    //加一秒按键
	ImageButton transmitButton;    //转发按键
	EditText commentEditTextView;//评论的输入框
	Button sendCommentButton;    //发送评论按键
	LinearLayout commentEdit;    //包裹评论输入框和发送键的布局
	LinearLayout tailLinearLayout;    //item的尾部布局
	private ImageView userAvatar;

	public HomeBaseViewHolder(View itemView) {
		super(itemView);
		userAvatar = (ImageView) itemView.findViewById(R.id.civ_avatar);
		tvName = (TextView) itemView.findViewById(R.id.tv_name);
		tvBody = (TextView) itemView.findViewById(R.id.tv_title);
		commentButton = (ImageButton) itemView.findViewById(R.id.ibtn_comment);
		addTimeButton = (ImageButton) itemView.findViewById(R.id.ibtn_add_time);
		transmitButton = (ImageButton) itemView.findViewById(R.id.ibtn_transmit);
		tailLinearLayout = (LinearLayout) itemView.findViewById(R.id.tail_linear_layout);
		commentEdit = (LinearLayout) itemView.findViewById(R.id.edit_comment);
		commentEditTextView = (EditText) itemView.findViewById(R.id.comment_edit_text_view);
		sendCommentButton = (Button) itemView.findViewById(R.id.send_comment_button);
	}

	public void bindView(final Context context, List<ContentBean> data, int position) {
		final ContentBean bean = data.get(position);
		//设置头像
		Glide.with(context).load(bean.getHead_image_url()).fitCenter().dontAnimate().into(userAvatar);

		tvName.setText(bean.getName());
		/*如果有转发信息，那么将正文界面隐藏起来（因为正文内容会在转发界面呈现）*/
		if (!bean.getRelayBeans().isEmpty()) {
			tvBody.setVisibility(View.GONE);
			addRelayText(context, tailLinearLayout, bean);
		}else{
			tvBody.setVisibility(View.VISIBLE);
			removeRelayText(tailLinearLayout);
		}

		setCommentVisAndLis(context);
	}



	/**
	 * 评论的相关监听以及可视设置
	 */
	private void setCommentVisAndLis(final Context context) {
		sendCommentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO
				Toast.makeText(context, "此处发送评论", Toast.LENGTH_SHORT).show();
			}
		});
		//在未点击评论键之前，评论框一直隐藏
		commentEdit.setVisibility(View.GONE);
		commentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				commentEdit.setVisibility(View.VISIBLE);
				commentEditTextView.requestFocus();
			}
		});
	}


	private void removeRelayText(ViewGroup parentView) {
		LinearLayout relayTextLayout;
		relayTextLayout = (LinearLayout) parentView.findViewById(R.id.relay_linear_layout);
		if (relayTextLayout != null) {
			parentView.removeViewAt(0);
		}
	}

	/**
	 * 添加与设置转发文字
	 *
	 * @param context     上下文
	 * @param parentView  转发布局的父布局
	 * @param contentBean item的数据包
	 */
	private void addRelayText(final Context context, ViewGroup parentView, ContentBean contentBean) {
		List<RelayBean> relayBeans = contentBean.getRelayBeans();
		LinearLayout relayTextLayout;
		boolean noChild = false;
		relayTextLayout = (LinearLayout) parentView.findViewById(R.id.relay_linear_layout);
		if (relayTextLayout == null) {
			noChild = true;
			relayTextLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.home_item_text_relay, null);
		}

		TextView originalTextView = (TextView) relayTextLayout.findViewById(R.id.tv_original_name_and_text);
		TextView relayTextView = (TextView) relayTextLayout.findViewById(R.id.tv_relay_name_and_text);
		/*
		 * 设置原贴的文字，以及原贴作者名点击事件
		 */
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(relayBeans.get(0).getName() + "\n");
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				//TODO
				Toast.makeText(context, "点击了用户名", Toast.LENGTH_SHORT).show();
			}
		};
		spannableStringBuilder.append(relayBeans.get(0).getContent());
		spannableStringBuilder.setSpan(clickableSpan, 0, relayBeans.get(0).getName().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		originalTextView.setText(spannableStringBuilder);
		//文字的点击事件要加上这一句，不然不会生效
		originalTextView.setMovementMethod(LinkMovementMethod.getInstance());

		//设置转发链
		SpannableStringBuilder spannableStringBuilderRelay = new SpannableStringBuilder("");

		//将当前用户(转发链的最后一个用户)单独设置，因为当前用户在转发链中不需要显示名字和冒号
		spannableStringBuilderRelay.append(relayBeans.get(relayBeans.size() - 1).getContent() );
		int lastIndex = relayBeans.get(relayBeans.size() - 1).getContent().length() + 2;

		for (int i = relayBeans.size() - 2; i >= 1; i--) {
			Log.d("relay1", relayBeans.get(i).getName()+" "+relayBeans.get(i).getContent());
			spannableStringBuilderRelay.append("\\\\" + relayBeans.get(i).getName() + ":");

			spannableStringBuilderRelay.append(relayBeans.get(i).getContent());
		}
		for (int i = relayBeans.size() - 2; i >= 1; i--) {
			final String name = relayBeans.get(i).getName();
			spannableStringBuilderRelay.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					Toast.makeText(context, "点击了"+name, Toast.LENGTH_SHORT).show();
				}
			}, lastIndex, lastIndex + relayBeans.get(i).getName().length(), 0);
			lastIndex += relayBeans.get(i).getName().length() + relayBeans.get(i).getContent().length() + 3;
		}
		relayTextView.setText(spannableStringBuilderRelay);
		//文字的点击事件要加上这一句，不然不会生效
		relayTextView.setMovementMethod(LinkMovementMethod.getInstance());
		if (noChild)
			parentView.addView(relayTextLayout, 0);

	}
}
