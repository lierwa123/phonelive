.
├── app
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.yunbao
│                   ├── AppContext.java
│                   ├── activity
│                   │   └── LauncherActivity.java
│                   └── wxapi
│                       ├── WXEntryActivity.java
│                       └── WXPayEntryActivity.java
├── baidu
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.baidu
│                   └── utils
│                       └── ImAsrUtil.java
├── beauty
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.beauty
│                   ├── custom
│                   │   ├── ItemDecoration2.java
│                   │   ├── MyRadioButton.java
│                   │   ├── SquareImageView2.java
│                   │   └── TextSeekBar.java
│                   ├── simple
│                   │   ├── SimpleBeautyEffectListener.java
│                   │   ├── SimpleBeautyViewHolder.java
│                   │   ├── SimpleFilterAdapter.java
│                   │   └── SimpleFilterBean.java
│                   ├── ui
│                   │   ├── adapter
│                   │   │   ├── BaseBeautyAdapter.java
│                   │   │   ├── BeautyAdapter.java
│                   │   │   ├── BeautyPagerAdapter.java
│                   │   │   ├── DefaultShapeAdapter.java
│                   │   │   ├── DistortionAdapter.java
│                   │   │   ├── FilterAdapter.java
│                   │   │   ├── QuickBeautyAdapter.java
│                   │   │   ├── ShapeAdapter.java
│                   │   │   ├── SpeciallyAdapter.java
│                   │   │   ├── SpeciallyPagerAdapter.java
│                   │   │   ├── StickerAdapter.java
│                   │   │   ├── StickerPagerAdapter.java
│                   │   │   └── WaterMarkAdapter.java
│                   │   ├── bean
│                   │   │   ├── BeautyBean.java
│                   │   │   ├── FilterBean.java
│                   │   │   ├── QuickBeautyBean.java
│                   │   │   ├── ShapeBean.java
│                   │   │   ├── SpeciallyBean.java
│                   │   │   ├── StickerBeautyBean.java
│                   │   │   ├── StickerCategaryBean.java
│                   │   │   ├── StickerServiceBean.java
│                   │   │   └── WatermarkBean.java
│                   │   ├── enums
│                   │   │   ├── BeautyTypeEnum.java
│                   │   │   ├── DistortionEnum.java
│                   │   │   ├── FilterEnum.java
│                   │   │   ├── QuickBeautyEnum.java
│                   │   │   └── QuickBeautyShapeEnum.java
│                   │   ├── filter
│                   │   │   └── MHFilter.java
│                   │   ├── interfaces
│                   │   │   ├── BeautyEffectListener.java
│                   │   │   ├── DefaultBeautyEffectListener.java
│                   │   │   ├── IBeautyViewHolder.java
│                   │   │   ├── MHBeautyEffectListener.java
│                   │   │   ├── MHCameraClickListener.java
│                   │   │   ├── OnItemClickListener.java
│                   │   │   ├── StickerCanClickListener.java
│                   │   │   └── StickerDataCallBack.java
│                   │   ├── manager
│                   │   │   └── StickerManager.java
│                   │   ├── util
│                   │   │   ├── ClickUtil.java
│                   │   │   └── DensityUtils.java
│                   │   └── views
│                   │       ├── AbsViewHolder.java
│                   │       ├── BadgeRadioButton
│                   │       │   ├── BadgeRadioButton.java
│                   │       │   └── DrawableCenterRadioButton.java
│                   │       ├── BaseBeautyViewHolder.java
│                   │       ├── BeautyDataModel.java
│                   │       ├── BeautyViewHolder.java
│                   │       ├── BeautyViewHolderFactory.java
│                   │       ├── DefaultBeautyViewHolder.java
│                   │       └── custom
│                   │           ├── ContentViewPager.java
│                   │           ├── ItemDecoration.java
│                   │           ├── LTabTextView.java
│                   │           ├── MyRadioButton.java
│                   │           ├── MyView.java
│                   │           ├── ScaleImageButton.java
│                   │           ├── TextSeekBar.java
│                   │           ├── TextSeekBarNew.java
│                   │           └── indicator
│                   │               ├── indicator
│                   │               │   ├── Indicator.java
│                   │               │   ├── slidebar
│                   │               │   │   ├── ColorBar.java
│                   │               │   │   ├── DrawableBar.java
│                   │               │   │   ├── LayoutBar.java
│                   │               │   │   └── ScrollBar.java
│                   │               │   └── transition
│                   │               │       └── OnTransitionTextListener.java
│                   │               └── utils
│                   │                   └── ColorGradient.java
│                   └── views
│                       ├── DefaultBeautyViewHolder.java
│                       └── MHProjectBeautyEffectListener.java
├── common
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.common
│                   ├── CommonAppConfig.java
│                   ├── CommonAppContext.java
│                   ├── Constants.java
│                   ├── HtmlConfig.java
│                   ├── activity
│                   │   ├── AbsActivity.java
│                   │   ├── ChooseImageActivity.java
│                   │   ├── ChooseLocationActivity.java
│                   │   ├── ChooseVideoActivity.java
│                   │   ├── ErrorActivity.java
│                   │   └── WebViewActivity.java
│                   ├── adapter
│                   │   ├── ChatChargeCoinAdapter.java
│                   │   ├── ChatChargePayAdapter.java
│                   │   ├── ChooseImageAdapter.java
│                   │   ├── ChooseLocationAdapter.java
│                   │   ├── ChooseVideoAdapter.java
│                   │   ├── ImChatFaceAdapter.java
│                   │   ├── ImChatFacePagerAdapter.java
│                   │   ├── ImagePreviewAdapter.java
│                   │   ├── RefreshAdapter.java
│                   │   └── ViewPagerAdapter.java
│                   ├── bean
│                   │   ├── AdBean.java
│                   │   ├── ChooseImageBean.java
│                   │   ├── ChooseVideoBean.java
│                   │   ├── CoinBean.java
│                   │   ├── CoinPayBean.java
│                   │   ├── ConfigBean.java
│                   │   ├── GoodsBean.java
│                   │   ├── LevelBean.java
│                   │   ├── LiveChatBean.java
│                   │   ├── LiveClassBean.java
│                   │   ├── LiveGiftBean.java
│                   │   ├── MeiyanConfig.java
│                   │   ├── TxLocationBean.java
│                   │   ├── TxLocationPoiBean.java
│                   │   ├── UserBean.java
│                   │   ├── UserItemBean.java
│                   │   └── VideoClassBean.java
│                   ├── custom
│                   │   ├── ActiveVoiceLayout.java
│                   │   ├── AppBarLayoutBehavior.java
│                   │   ├── BubbleLayout.java
│                   │   ├── CircleProgress.java
│                   │   ├── CoinGiveLayout.java
│                   │   ├── CommonRefreshView.java
│                   │   ├── DrawGiftView.java
│                   │   ├── DrawableCheckBox.java
│                   │   ├── DrawableRadioButton.java
│                   │   ├── DrawableRadioButton2.java
│                   │   ├── DrawableTextView.java
│                   │   ├── FixAppBarLayoutBehavior.java
│                   │   ├── HomeIndicatorTitle.java
│                   │   ├── ItemDecoration.java
│                   │   ├── ItemSlideHelper.java
│                   │   ├── LineProgress.java
│                   │   ├── MyFrameLayout1.java
│                   │   ├── MyFrameLayout2.java
│                   │   ├── MyImageView2.java
│                   │   ├── MyLinearLayout1.java
│                   │   ├── MyLinearLayout2.java
│                   │   ├── MyLinearLayout3.java
│                   │   ├── MyLinearLayout4.java
│                   │   ├── MyLinearLayout5.java
│                   │   ├── MyLinearLayout6.java
│                   │   ├── MyRadioButton.java
│                   │   ├── MyRelativeLayout2.java
│                   │   ├── MyRelativeLayout5.java
│                   │   ├── MyRelativeLayout6.java
│                   │   ├── MyRelativeLayout7.java
│                   │   ├── MyViewPager.java
│                   │   ├── NineGridLayout.java
│                   │   ├── NineGridLayout2.java
│                   │   ├── RatingBar.java
│                   │   ├── RatioImageView.java
│                   │   ├── RatioRoundImageView.java
│                   │   ├── SquareImageView.java
│                   │   ├── SquareRoundedImageView.java
│                   │   ├── StarCountView.java
│                   │   ├── TabButton.java
│                   │   ├── TabButtonGroup.java
│                   │   └── VerticalImageSpan.java
│                   ├── dialog
│                   │   ├── AbsDialogFragment.java
│                   │   ├── ActiveVideoPreviewDialog.java
│                   │   ├── ChatFaceDialog.java
│                   │   ├── ImagePreviewDialog.java
│                   │   ├── LiveChargeDialogFragment.java
│                   │   ├── LiveChargePayDialogFragment.java
│                   │   ├── NotCancelableDialog.java
│                   │   └── VideoPreviewDialog.java
│                   ├── event
│                   │   ├── CoinChangeEvent.java
│                   │   ├── FollowEvent.java
│                   │   ├── LocationEvent.java
│                   │   ├── LoginInvalidEvent.java
│                   │   └── UpdateFieldEvent.java
│                   ├── fragment
│                   │   └── ProcessFragment.java
│                   ├── glide
│                   │   └── ImgLoader.java
│                   ├── http
│                   │   ├── CommonHttpConsts.java
│                   │   ├── CommonHttpUtil.java
│                   │   ├── Data.java
│                   │   ├── HttpCallback.java
│                   │   ├── HttpClient.java
│                   │   ├── HttpLoggingInterceptor.java
│                   │   └── JsonBean.java
│                   ├── interfaces
│                   │   ├── ActivityResultCallback.java
│                   │   ├── CommonCallback.java
│                   │   ├── ImageResultCallback.java
│                   │   ├── KeyBoardHeightChangeListener.java
│                   │   ├── LifeCycleListener.java
│                   │   ├── OnFaceClickListener.java
│                   │   ├── OnItemClickListener.java
│                   │   └── VideoResultCallback.java
│                   ├── mob
│                   │   ├── LoginData.java
│                   │   ├── MobBean.java
│                   │   ├── MobCallback.java
│                   │   ├── MobConst.java
│                   │   ├── MobLoginUtil.java
│                   │   ├── MobShareUtil.java
│                   │   └── ShareData.java
│                   ├── pay
│                   │   ├── PayCallback.java
│                   │   ├── PayPresenter.java
│                   │   ├── ali
│                   │   │   ├── AliPayBuilder.java
│                   │   │   ├── Base64.java
│                   │   │   └── SignUtils.java
│                   │   └── wx
│                   │       ├── WxApiWrapper.java
│                   │       └── WxPayBuilder.java
│                   ├── upload
│                   │   ├── UploadBean.java
│                   │   ├── UploadCallback.java
│                   │   ├── UploadQnImpl.java
│                   │   └── UploadStrategy.java
│                   ├── utils
│                   │   ├── BitmapUtil.java
│                   │   ├── CalculateUtil.java
│                   │   ├── ChooseImageUtil.java
│                   │   ├── ChooseVideoUtil.java
│                   │   ├── CityUtil.java
│                   │   ├── ClickUtil.java
│                   │   ├── CommonIconUtil.java
│                   │   ├── DateFormatUtil.java
│                   │   ├── DecryptUtil.java
│                   │   ├── DialogUitl.java
│                   │   ├── DownloadUtil.java
│                   │   ├── DpUtil.java
│                   │   ├── FaceUtil.java
│                   │   ├── FileUtil.java
│                   │   ├── GifCacheUtil.java
│                   │   ├── GlideCatchUtil.java
│                   │   ├── IntentHelper.java
│                   │   ├── JsonUtil.java
│                   │   ├── KeyBoardHeightUtil.java
│                   │   ├── KeyBoardHeightUtil2.java
│                   │   ├── L.java
│                   │   ├── LocationUtil.java
│                   │   ├── LogUtil.java
│                   │   ├── MD5Util.java
│                   │   ├── ProcessImageUtil.java
│                   │   ├── ProcessResultUtil.java
│                   │   ├── RandomUtil.java
│                   │   ├── RouteUtil.java
│                   │   ├── ScreenDimenUtil.java
│                   │   ├── SpUtil.java
│                   │   ├── StringUtil.java
│                   │   ├── SystemUtil.java
│                   │   ├── ToastUtil.java
│                   │   ├── ValidatePhoneUtil.java
│                   │   ├── VersionUtil.java
│                   │   ├── WordFilterUtil.java
│                   │   ├── WordUtil.java
│                   │   └── XmlUtil.java
│                   └── views
│                       ├── AbsCommonViewHolder.java
│                       ├── AbsLivePageViewHolder.java
│                       └── AbsViewHolder.java
├── game
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.game
│                   ├── GameConsts.java
│                   ├── adapter
│                   │   ├── GameAdapter.java
│                   │   ├── GameNzLsAdapter.java
│                   │   ├── GameNzSfAdapter.java
│                   │   └── GameNzSzAdapter.java
│                   ├── bean
│                   │   ├── BankerBean.java
│                   │   ├── GameNzLsBean.java
│                   │   ├── GameNzSzBean.java
│                   │   └── GameParam.java
│                   ├── custom
│                   │   ├── GameBetCoinView.java
│                   │   ├── GameEbbView.java
│                   │   ├── LuckPanView.java
│                   │   ├── LuckPanWrap.java
│                   │   ├── LuckPanWrap2.java
│                   │   ├── PokerView.java
│                   │   └── ZpBetView.java
│                   ├── dialog
│                   │   ├── GameDialogFragment.java
│                   │   ├── GameNzLsDialogFragment.java
│                   │   ├── GameNzSfDialogFragment.java
│                   │   └── GameNzSzDialogFragment.java
│                   ├── event
│                   │   ├── GameWindowChangedEvent.java
│                   │   └── OpenGameChargeEvent.java
│                   ├── http
│                   │   ├── GameHttpConsts.java
│                   │   └── GameHttpUtil.java
│                   ├── interfaces
│                   │   └── GameActionListener.java
│                   ├── util
│                   │   ├── GameIconUtil.java
│                   │   ├── GamePresenter.java
│                   │   └── GameSoundPool.java
│                   └── views
│                       ├── AbsGameViewHolder.java
│                       ├── GameEbbViewHolder.java
│                       ├── GameHdViewHolder.java
│                       ├── GameNzResultViewHolder.java
│                       ├── GameNzViewHolder.java
│                       ├── GameZjhViewHolder.java
│                       ├── GameZpResultViewHolder.java
│                       └── GameZpViewHolder.java
├── im
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.im
│                   ├── activity
│                   │   ├── ChatActivity.java
│                   │   ├── ChatChooseImageActivity.java
│                   │   ├── ChatRoomActivity.java
│                   │   ├── LocationActivity.java
│                   │   └── SystemMessageActivity.java
│                   ├── adapter
│                   │   ├── ChatImagePreviewAdapter.java
│                   │   ├── ImChatChooseImageAdapter.java
│                   │   ├── ImListAdapter.java
│                   │   ├── ImRoomAdapter.java
│                   │   ├── LocationAdapter.java
│                   │   └── SystemMessageAdapter.java
│                   ├── bean
│                   │   ├── ChatChooseImageBean.java
│                   │   ├── ImChatImageBean.java
│                   │   ├── ImMessageBean.java
│                   │   ├── ImMsgLocationBean.java
│                   │   ├── ImUserBean.java
│                   │   ├── SystemMessageBean.java
│                   │   └── TimeInfo.java
│                   ├── custom
│                   │   ├── ChatVoiceLayout.java
│                   │   ├── MyImageView.java
│                   │   └── MyRelativeLayout.java
│                   ├── dialog
│                   │   ├── ChatImageDialog.java
│                   │   ├── ChatMoreDialog.java
│                   │   ├── ChatVoiceInputDialog.java
│                   │   └── SystemMessageDialogFragment.java
│                   ├── event
│                   │   ├── ImLoginEvent.java
│                   │   ├── ImOffLineMsgEvent.java
│                   │   ├── ImRoamMsgEvent.java
│                   │   ├── ImUnReadCountEvent.java
│                   │   ├── ImUserMsgEvent.java
│                   │   └── SystemMsgEvent.java
│                   ├── http
│                   │   ├── ImHttpConsts.java
│                   │   └── ImHttpUtil.java
│                   ├── interfaces
│                   │   ├── ChatRoomActionListener.java
│                   │   ├── ImClient.java
│                   │   └── SendMsgResultCallback.java
│                   ├── receiver
│                   │   └── JPushReceiver.java
│                   ├── utils
│                   │   ├── ImDateUtil.java
│                   │   ├── ImMessageUtil.java
│                   │   ├── ImPushUtil.java
│                   │   ├── ImTextRender.java
│                   │   ├── ImageUtil.java
│                   │   ├── JimMessageUtil.java
│                   │   ├── MediaRecordUtil.java
│                   │   └── VoiceMediaPlayerUtil.java
│                   └── views
│                       ├── ChatListViewHolder.java
│                       ├── ChatRoomDialogViewHolder.java
│                       ├── ChatRoomViewHolder.java
│                       └── SystemMessageViewHolder.java
├── live
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.live
│                   ├── LiveConfig.java
│                   ├── activity
│                   │   ├── LiveActivity.java
│                   │   ├── LiveAddImpressActivity.java
│                   │   ├── LiveAdminListActivity.java
│                   │   ├── LiveAnchorActivity.java
│                   │   ├── LiveAudienceActivity.java
│                   │   ├── LiveBlackActivity.java
│                   │   ├── LiveChooseClassActivity.java
│                   │   ├── LiveContributeActivity.java
│                   │   ├── LiveGoodsAddActivity.java
│                   │   ├── LiveGuardListActivity.java
│                   │   ├── LiveRecordActivity.java
│                   │   ├── LiveRecordPlayActivity.java
│                   │   ├── LiveReportActivity.java
│                   │   ├── LiveShutUpActivity.java
│                   │   ├── RoomManageActivity.java
│                   │   └── RoomManageDetailActivity.java
│                   ├── adapter
│                   │   ├── DailyTaskAdapter.java
│                   │   ├── GuardAdapter.java
│                   │   ├── GuardRightAdapter.java
│                   │   ├── LiveAdminListAdapter.java
│                   │   ├── LiveAdminRoomAdapter.java
│                   │   ├── LiveBlackAdapter.java
│                   │   ├── LiveChatAdapter.java
│                   │   ├── LiveFunctionAdapter.java
│                   │   ├── LiveGiftAdapter.java
│                   │   ├── LiveGiftCountAdapter.java
│                   │   ├── LiveGiftPagerAdapter.java
│                   │   ├── LiveGoodsAdapter.java
│                   │   ├── LiveGoodsAddAdapter.java
│                   │   ├── LivePkAdapter.java
│                   │   ├── LiveReadyClassAdapter.java
│                   │   ├── LiveReadyShareAdapter.java
│                   │   ├── LiveRecordAdapter.java
│                   │   ├── LiveReportAdapter.java
│                   │   ├── LiveRoomScrollAdapter.java
│                   │   ├── LiveRoomTypeAdapter.java
│                   │   ├── LiveShareAdapter.java
│                   │   ├── LiveShopAdapter.java
│                   │   ├── LiveShutUpAdapter.java
│                   │   ├── LiveTimeChargeAdapter.java
│                   │   ├── LiveUserAdapter.java
│                   │   ├── LuckPanRecordAdapter.java
│                   │   ├── LuckPanWinAdapter.java
│                   │   ├── RedPackAdapter.java
│                   │   └── RedPackResultAdapter.java
│                   ├── bean
│                   │   ├── BackPackGiftBean.java
│                   │   ├── DailyTaskBean.java
│                   │   ├── GlobalGiftBean.java
│                   │   ├── GuardBuyBean.java
│                   │   ├── GuardRightBean.java
│                   │   ├── GuardUserBean.java
│                   │   ├── ImpressBean.java
│                   │   ├── LiveAdminRoomBean.java
│                   │   ├── LiveBean.java
│                   │   ├── LiveBuyGuardMsgBean.java
│                   │   ├── LiveChatBean.java
│                   │   ├── LiveConfigBean.java
│                   │   ├── LiveDanMuBean.java
│                   │   ├── LiveEnterRoomBean.java
│                   │   ├── LiveFunctionBean.java
│                   │   ├── LiveGiftPrizePoolWinBean.java
│                   │   ├── LiveGuardInfo.java
│                   │   ├── LiveLuckGiftWinBean.java
│                   │   ├── LivePkBean.java
│                   │   ├── LiveReceiveGiftBean.java
│                   │   ├── LiveRecordBean.java
│                   │   ├── LiveReportBean.java
│                   │   ├── LiveRoomTypeBean.java
│                   │   ├── LiveShutUpBean.java
│                   │   ├── LiveTimeChargeBean.java
│                   │   ├── LiveUserGiftBean.java
│                   │   ├── LuckPanBean.java
│                   │   ├── RedPackBean.java
│                   │   ├── RedPackResultBean.java
│                   │   ├── SearchUserBean.java
│                   │   ├── TiFilter.java
│                   │   ├── TurntableConfigBean.java
│                   │   └── TurntableGiftBean.java
│                   ├── custom
│                   │   ├── FrameImageView.java
│                   │   ├── GiftMarkView.java
│                   │   ├── GiftPageViewPager.java
│                   │   ├── LiveAudienceRecyclerView.java
│                   │   ├── LiveLightView.java
│                   │   ├── MusicProgressTextView.java
│                   │   ├── MyFrameLayout3.java
│                   │   ├── MyFrameLayout4.java
│                   │   ├── MyImageView.java
│                   │   ├── MyRelativeLayout1.java
│                   │   ├── MyTextView.java
│                   │   ├── MyTextView2.java
│                   │   ├── PkProgressBar.java
│                   │   ├── ProgressTextView.java
│                   │   ├── StarView.java
│                   │   └── TopGradual.java
│                   ├── dialog
│                   │   ├── DailyTaskDialogFragment.java
│                   │   ├── GiftPrizePoolFragment.java
│                   │   ├── LiveChatListDialogFragment.java
│                   │   ├── LiveChatRoomDialogFragment.java
│                   │   ├── LiveFunctionDialogFragment.java
│                   │   ├── LiveGiftDialogFragment.java
│                   │   ├── LiveGoodsDialogFragment.java
│                   │   ├── LiveGuardBuyDialogFragment.java
│                   │   ├── LiveGuardDialogFragment.java
│                   │   ├── LiveInputDialogFragment.java
│                   │   ├── LiveLinkMicListDialogFragment.java
│                   │   ├── LiveLinkMicPkSearchDialog.java
│                   │   ├── LiveRedPackListDialogFragment.java
│                   │   ├── LiveRedPackResultDialogFragment.java
│                   │   ├── LiveRedPackRobDialogFragment.java
│                   │   ├── LiveRedPackSendDialogFragment.java
│                   │   ├── LiveRoomCheckDialogFragment.java
│                   │   ├── LiveRoomCheckDialogFragment2.java
│                   │   ├── LiveRoomTypeDialogFragment.java
│                   │   ├── LiveShareDialogFragment.java
│                   │   ├── LiveShopDialogFragment.java
│                   │   ├── LiveTimeDialogFragment.java
│                   │   ├── LiveUserDialogFragment.java
│                   │   ├── LuckPanDialogFragment.java
│                   │   ├── LuckPanRecordDialogFragment.java
│                   │   ├── LuckPanTipDialogFragment.java
│                   │   └── LuckPanWinDialogFragment.java
│                   ├── event
│                   │   ├── LinkMicTxAccEvent.java
│                   │   ├── LinkMicTxMixStreamEvent.java
│                   │   └── LiveRoomChangeEvent.java
│                   ├── http
│                   │   ├── LiveHttpConsts.java
│                   │   ├── LiveHttpUtil.java
│                   │   └── MusicUrlCallback.java
│                   ├── interfaces
│                   │   ├── ILiveLinkMicViewHolder.java
│                   │   ├── ILivePushViewHolder.java
│                   │   ├── LiveFunctionClickListener.java
│                   │   ├── LivePushListener.java
│                   │   └── RedPackCountDownListener.java
│                   ├── music
│                   │   ├── LiveMusicAdapter.java
│                   │   ├── LiveMusicBean.java
│                   │   ├── LiveMusicDialogFragment.java
│                   │   ├── LiveMusicPlayer.java
│                   │   ├── LrcBean.java
│                   │   ├── LrcParser.java
│                   │   ├── LrcTextView.java
│                   │   └── db
│                   │       ├── MusicDbHelper.java
│                   │       └── MusicDbManager.java
│                   ├── presenter
│                   │   ├── LiveDanmuPresenter.java
│                   │   ├── LiveEnterRoomAnimPresenter.java
│                   │   ├── LiveGiftAnimPresenter.java
│                   │   ├── LiveLightAnimPresenter.java
│                   │   ├── LiveLinkMicAnchorPresenter.java
│                   │   ├── LiveLinkMicPkPresenter.java
│                   │   ├── LiveLinkMicPresenter.java
│                   │   ├── LiveRoomCheckLivePresenter.java
│                   │   ├── LiveRoomCheckLivePresenter2.java
│                   │   └── UserHomeSharePresenter.java
│                   ├── socket
│                   │   ├── GameActionListenerImpl.java
│                   │   ├── SocketChatUtil.java
│                   │   ├── SocketClient.java
│                   │   ├── SocketGameUtil.java
│                   │   ├── SocketLinkMicAnchorUtil.java
│                   │   ├── SocketLinkMicPkUtil.java
│                   │   ├── SocketLinkMicUtil.java
│                   │   ├── SocketMessageListener.java
│                   │   ├── SocketReceiveBean.java
│                   │   └── SocketSendBean.java
│                   ├── utils
│                   │   ├── LiveIconUtil.java
│                   │   ├── LiveStorge.java
│                   │   └── LiveTextRender.java
│                   └── views
│                       ├── AbsLiveGiftViewHolder.java
│                       ├── AbsLiveLinkMicPlayViewHolder.java
│                       ├── AbsLiveLinkMicPushViewHolder.java
│                       ├── AbsLivePushViewHolder.java
│                       ├── AbsLiveViewHolder.java
│                       ├── AbsUserHomeViewHolder.java
│                       ├── DanmuViewHolder.java
│                       ├── LauncherAdViewHolder.java
│                       ├── LiveAddImpressViewHolder.java
│                       ├── LiveAdminListViewHolder.java
│                       ├── LiveAnchorViewHolder.java
│                       ├── LiveAudienceViewHolder.java
│                       ├── LiveContributeViewHolder.java
│                       ├── LiveEndViewHolder.java
│                       ├── LiveGiftDaoViewHolder.java
│                       ├── LiveGiftDrawViewHolder.java
│                       ├── LiveGiftGiftViewHolder.java
│                       ├── LiveGiftLuckTopViewHolder.java
│                       ├── LiveGiftPackageViewHolder.java
│                       ├── LiveGiftPrizePoolViewHolder.java
│                       ├── LiveGiftViewHolder.java
│                       ├── LiveGoodsAddViewHolder.java
│                       ├── LiveLinkMicPkViewHolder.java
│                       ├── LiveLinkMicPlayKsyViewHolder.java
│                       ├── LiveLinkMicPlayTxViewHolder.java
│                       ├── LiveLinkMicPushKsyViewHolder.java
│                       ├── LiveLinkMicPushTxViewHolder.java
│                       ├── LiveMusicViewHolder.java
│                       ├── LiveMyLiveRoomViewHolder.java
│                       ├── LiveMyRoomViewHolder.java
│                       ├── LivePlayKsyViewHolder.java
│                       ├── LivePlayTxViewHolder.java
│                       ├── LivePushKsyViewHolder.java
│                       ├── LivePushTxViewHolder.java
│                       ├── LiveReadyViewHolder.java
│                       ├── LiveRecordPlayViewHolder.java
│                       ├── LiveRecordViewHolder.java
│                       ├── LiveRoomBtnViewHolder.java
│                       ├── LiveRoomPlayViewHolder.java
│                       ├── LiveRoomViewHolder.java
│                       ├── LiveTitleAnimViewHolder.java
│                       ├── LiveWebViewHolder.java
│                       └── LuckLiveGiftViewHolder.java
├── local.properties
├── main
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.main
│                   ├── activity
│                   │   ├── ActiveAllTopicActivity.java
│                   │   ├── ActiveChooseTopicActivity.java
│                   │   ├── ActiveDetailActivity.java
│                   │   ├── ActivePubActivity.java
│                   │   ├── ActiveReportActivity.java
│                   │   ├── ActiveSearchTopicActivity.java
│                   │   ├── ActiveTopicActivity.java
│                   │   ├── ActiveVideoPlayActivity.java
│                   │   ├── ActiveVideoRecordActivity.java
│                   │   ├── CancelAccountActivity.java
│                   │   ├── CancelConditionActivity.java
│                   │   ├── CashActivity.java
│                   │   ├── DailyTaskActivity.java
│                   │   ├── EditNameActivity.java
│                   │   ├── EditProfileActivity.java
│                   │   ├── EditSexActivity.java
│                   │   ├── EditSignActivity.java
│                   │   ├── FansActivity.java
│                   │   ├── FindPwdActivity.java
│                   │   ├── FollowActivity.java
│                   │   ├── GoodsAddActivity.java
│                   │   ├── LiveClassActivity.java
│                   │   ├── LiveRecommendActivity.java
│                   │   ├── LoginActivity.java
│                   │   ├── LoginInvalidActivity.java
│                   │   ├── MainActivity.java
│                   │   ├── MallClassActivity.java
│                   │   ├── MallSearchActivity.java
│                   │   ├── ModifyPwdActivity.java
│                   │   ├── MyActiveActivity.java
│                   │   ├── MyCoinActivity.java
│                   │   ├── MyImpressActivity.java
│                   │   ├── MyProfitActivity.java
│                   │   ├── MyVideoActivity.java
│                   │   ├── RankActivity.java
│                   │   ├── RecommendActivity.java
│                   │   ├── RegisterActivity.java
│                   │   ├── SearchActivity.java
│                   │   ├── SettingActivity.java
│                   │   ├── ShopActivity.java
│                   │   ├── ShopGoodsActivity.java
│                   │   ├── TestActivity.java
│                   │   ├── ThreeDistributActivity.java
│                   │   └── UserHomeActivity.java
│                   ├── adapter
│                   │   ├── ActiveAdapter.java
│                   │   ├── ActiveAllTopicAdapter.java
│                   │   ├── ActiveChooseTopicAdapter.java
│                   │   ├── ActiveCommentAdapter.java
│                   │   ├── ActiveHotTopicAdapter.java
│                   │   ├── ActiveImageAdapter.java
│                   │   ├── ActiveRecomTopicAdapter.java
│                   │   ├── ActiveSearchTopicAdapter.java
│                   │   ├── CancelConditionAdapter.java
│                   │   ├── CashAccountAdapter.java
│                   │   ├── CoinAdapter.java
│                   │   ├── CoinPayAdapter.java
│                   │   ├── LiveShareAdapter.java
│                   │   ├── LoginTypeAdapter.java
│                   │   ├── MainHomeFollowAdapter.java
│                   │   ├── MainHomeLiveAdapter.java
│                   │   ├── MainHomeLiveClassAdapter.java
│                   │   ├── MainHomeLiveRecomAdapter.java
│                   │   ├── MainHomeNearAdapter.java
│                   │   ├── MainHomeVideoAdapter.java
│                   │   ├── MainHomeVideoClassAdapter.java
│                   │   ├── MainListAdapter.java
│                   │   ├── MainMallAdapter.java
│                   │   ├── MainMallClassAdapter.java
│                   │   ├── MainMeAdapter.java
│                   │   ├── MallClassAdapter.java
│                   │   ├── MallSearchAdapter.java
│                   │   ├── RecommendAdapter.java
│                   │   ├── SearchAdapter.java
│                   │   ├── SettingAdapter.java
│                   │   ├── ShopAdapter.java
│                   │   └── VideoHomeAdapter.java
│                   ├── bean
│                   │   ├── ActiveBean.java
│                   │   ├── ActiveCommentBean.java
│                   │   ├── ActiveImageBean.java
│                   │   ├── ActiveTopicBean.java
│                   │   ├── ActiveUserBean.java
│                   │   ├── BannerBean.java
│                   │   ├── BonusBean.java
│                   │   ├── CancelConditionBean.java
│                   │   ├── CashAccountBean.java
│                   │   ├── ListBean.java
│                   │   ├── RecommendBean.java
│                   │   ├── SettingBean.java
│                   │   ├── StoreBean.java
│                   │   └── UserHomeConBean.java
│                   ├── custom
│                   │   ├── ActiveLikeImage.java
│                   │   └── BonusItemView.java
│                   ├── dialog
│                   │   ├── ActiveInputDialogFragment.java
│                   │   ├── LoginForbiddenDialogFragment.java
│                   │   ├── LoginTipDialogFragment.java
│                   │   └── MainStartDialogFragment.java
│                   ├── event
│                   │   ├── ActiveCommentEvent.java
│                   │   ├── ActiveDeleteEvent.java
│                   │   ├── ActiveLikeEvent.java
│                   │   └── RegSuccessEvent.java
│                   ├── http
│                   │   ├── MainHttpConsts.java
│                   │   └── MainHttpUtil.java
│                   ├── interfaces
│                   │   ├── AppBarStateListener.java
│                   │   ├── DataLoader.java
│                   │   ├── MainAppBarExpandListener.java
│                   │   ├── MainAppBarLayoutListener.java
│                   │   └── MainStartChooseCallback.java
│                   ├── presenter
│                   │   └── CheckLivePresenter.java
│                   ├── utils
│                   │   ├── AudioRecorderEx.java
│                   │   ├── FilePathUtil.java
│                   │   └── MainIconUtil.java
│                   └── views
│                       ├── AbsMainActiveViewHolder.java
│                       ├── AbsMainHomeChildViewHolder.java
│                       ├── AbsMainHomeParentViewHolder.java
│                       ├── AbsMainListChildViewHolder.java
│                       ├── AbsMainViewHolder.java
│                       ├── ActiveHomeViewHolder.java
│                       ├── ActiveRecordVoiceViewHolder.java
│                       ├── ActiveRecordVoiceViewHolder2.java
│                       ├── BonusViewHolder.java
│                       ├── CashAccountViewHolder.java
│                       ├── GoodsAddTaoBaoViewHolder.java
│                       ├── GoodsAddXcxViewHolder.java
│                       ├── MainActiveFollowViewHolder.java
│                       ├── MainActiveNewestViewHolder.java
│                       ├── MainActiveRecommendViewHolder.java
│                       ├── MainActiveViewHolder.java
│                       ├── MainHomeFollowViewHolder.java
│                       ├── MainHomeLiveViewHolder.java
│                       ├── MainHomeNearViewHolder.java
│                       ├── MainHomeVideoViewHolder.java
│                       ├── MainHomeViewHolder.java
│                       ├── MainListContributeViewHolder.java
│                       ├── MainListProfitViewHolder.java
│                       ├── MainListViewHolder.java
│                       ├── MainMallViewHolder.java
│                       ├── MainMeViewHolder.java
│                       ├── UserHomeDetailViewHolder.java
│                       ├── UserHomeViewHolder.java
│                       ├── UserHomeViewHolder2.java
│                       └── VideoHomeViewHolder.java
├── mall
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com
│                   └── yunbao
│                       └── mall
│                   ├── activity
│                   │   ├── BuyerAccountActivity.java
│                   │   ├── BuyerActivity.java
│                   │   ├── BuyerAddressActivity.java
│                   │   ├── BuyerAddressEditActivity.java
│                   │   ├── BuyerCashActivity.java
│                   │   ├── BuyerCommentActivity.java
│                   │   ├── BuyerCommentAppendActivity.java
│                   │   ├── BuyerOrderActivity.java
│                   │   ├── BuyerOrderDetailActivity.java
│                   │   ├── BuyerRefundApplyActivity.java
│                   │   ├── BuyerRefundDetailActivity.java
│                   │   ├── BuyerRefundOfficialActivity.java
│                   │   ├── ChooseGoodsClassActivity.java
│                   │   ├── GoodsAddOutSideActivity.java
│                   │   ├── GoodsCommentActivity.java
│                   │   ├── GoodsDetailActivity.java
│                   │   ├── GoodsEditSpecActivity.java
│                   │   ├── GoodsMakeOrderActivity.java
│                   │   ├── GoodsOutSideDetailActivity.java
│                   │   ├── GoodsRecordActivity.java
│                   │   ├── GoodsSearchActivity.java
│                   │   ├── ManageClassActivity.java
│                   │   ├── OrderMessageActivity.java
│                   │   ├── PayContentActivity1.java
│                   │   ├── PayContentActivity2.java
│                   │   ├── PayContentClassActivity.java
│                   │   ├── PayContentDetailActivity.java
│                   │   ├── PayContentPubActivity.java
│                   │   ├── SellerAccountActivity.java
│                   │   ├── SellerActivity.java
│                   │   ├── SellerAddGoodsActivity.java
│                   │   ├── SellerAddressActivity.java
│                   │   ├── SellerAddressEditActivity.java
│                   │   ├── SellerCashActivity.java
│                   │   ├── SellerManageGoodsActivity.java
│                   │   ├── SellerOrderActivity.java
│                   │   ├── SellerOrderDetailActivity.java
│                   │   ├── SellerRefundDetailActivity.java
│                   │   ├── SellerRejectRefundActivity.java
│                   │   ├── SellerSendActivity.java
│                   │   ├── ShopApplyActivity.java
│                   │   ├── ShopApplyBondActivity.java
│                   │   ├── ShopApplyResultActivity.java
│                   │   ├── ShopCertActivity.java
│                   │   ├── ShopDetailActivity.java
│                   │   └── ShopHomeActivity.java
│                   ├── adapter
│                   │   ├── AddGoodsCommentAdapter.java
│                   │   ├── AddGoodsDetailAdapter.java
│                   │   ├── AddGoodsSpecAdapter.java
│                   │   ├── AddGoodsTitleAdapter.java
│                   │   ├── BuyerAddressAdapter.java
│                   │   ├── BuyerOrderAllAdapter.java
│                   │   ├── BuyerOrderBaseAdapter.java
│                   │   ├── BuyerOrderCommentAdapter.java
│                   │   ├── BuyerOrderPayAdapter.java
│                   │   ├── BuyerOrderReceiveAdapter.java
│                   │   ├── BuyerOrderRefundAdapter.java
│                   │   ├── BuyerOrderSendAdapter.java
│                   │   ├── BuyerRefundRecordAdapter.java
│                   │   ├── GoodsChooseSpecAdapter.java
│                   │   ├── GoodsClassLeftAdapter.java
│                   │   ├── GoodsClassRightAdapter.java
│                   │   ├── GoodsCommentAdapter.java
│                   │   ├── GoodsCommentTypeAdapter.java
│                   │   ├── GoodsDetailAdapter.java
│                   │   ├── GoodsEditSpecAdapter.java
│                   │   ├── GoodsPayAdapter.java
│                   │   ├── GoodsRecordAdapter.java
│                   │   ├── GoodsTitleAdapter.java
│                   │   ├── ManageClassAdapter.java
│                   │   ├── OrderMessageAdapter.java
│                   │   ├── PayBuyAdapter.java
│                   │   ├── PayContentClassAdapter.java
│                   │   ├── PayContentMulAdapter.java
│                   │   ├── PayContentVideoPlayAdapter.java
│                   │   ├── PayPubAdapter.java
│                   │   ├── SearchGoodsAdapter.java
│                   │   ├── SearchPayAdapter.java
│                   │   ├── SellerOrderAllAdapter.java
│                   │   ├── SellerOrderAllRefundAdapter.java
│                   │   ├── SellerOrderBaseAdapter.java
│                   │   ├── SellerOrderFinishAdapter.java
│                   │   ├── SellerOrderPayAdapter.java
│                   │   ├── SellerOrderReceiveAdapter.java
│                   │   ├── SellerOrderRefundAdapter.java
│                   │   ├── SellerOrderSendAdapter.java
│                   │   ├── SellerShenHeAdapter.java
│                   │   ├── SellerWuliuAdapter.java
│                   │   ├── SellerXiaJiaAdapter.java
│                   │   ├── SellerZaiShouAdapter.java
│                   │   └── ShopHomeAdapter.java
│                   ├── bean
│                   │   ├── AddGoodsCommentImageBean.java
│                   │   ├── AddGoodsImageBean.java
│                   │   ├── AddGoodsSpecBean.java
│                   │   ├── BuyerAddressBean.java
│                   │   ├── BuyerOrderBean.java
│                   │   ├── BuyerRefundRecordBean.java
│                   │   ├── GoodsChooseSpecBean.java
│                   │   ├── GoodsClassBean.java
│                   │   ├── GoodsClassTitleBean.java
│                   │   ├── GoodsCommentBean.java
│                   │   ├── GoodsCommentTypeBean.java
│                   │   ├── GoodsHomeClassBean.java
│                   │   ├── GoodsManageBean.java
│                   │   ├── GoodsPayBean.java
│                   │   ├── GoodsRecordBean.java
│                   │   ├── GoodsRecordItemBean.java
│                   │   ├── GoodsRecordTitleBean.java
│                   │   ├── GoodsSimpleBean.java
│                   │   ├── GoodsSpecBean.java
│                   │   ├── ManageClassBean.java
│                   │   ├── OrderMsgBean.java
│                   │   ├── PayContentBean.java
│                   │   ├── PayContentBuyBean.java
│                   │   ├── PayContentClassBean.java
│                   │   ├── PayContentVideoBean.java
│                   │   ├── PayContentVideoPlayBean.java
│                   │   ├── RefundReasonBean.java
│                   │   ├── RejectRefundBean.java
│                   │   ├── SearchGoodsBean.java
│                   │   ├── SearchPayBean.java
│                   │   ├── SellerOrderBean.java
│                   │   └── WuliuBean.java
│                   ├── dialog
│                   │   ├── BuyerRefundReasonDialogFragment.java
│                   │   ├── GoodsCertDialogFragment.java
│                   │   ├── GoodsPayDialogFragment.java
│                   │   ├── GoodsSpecDialogFragment.java
│                   │   ├── OfficialRefundReasonDialogFragment.java
│                   │   ├── PayCommentDialogFragment.java
│                   │   ├── PayContentPayDialogFragment.java
│                   │   ├── PayContentResultDialogFragment.java
│                   │   ├── PayContentTipDialogFragment.java
│                   │   └── SellerRejectReasonDialogFragment.java
│                   ├── http
│                   │   ├── MallHttpConsts.java
│                   │   └── MallHttpUtil.java
│                   └── views
│                       ├── AbsBuyerOrderViewHolder.java
│                       ├── AbsSellerOrderViewHolder.java
│                       ├── BuyerOrderAllViewHolder.java
│                       ├── BuyerOrderCommentViewHolder.java
│                       ├── BuyerOrderPayViewHolder.java
│                       ├── BuyerOrderReceiveViewHolder.java
│                       ├── BuyerOrderRefundViewHolder.java
│                       ├── BuyerOrderSendViewHolder.java
│                       ├── PayBuyViewHolder.java
│                       ├── PayContentChooseVideoViewHolder.java
│                       ├── PayContentMulViewHolder.java
│                       ├── PayContentSingleViewHolder.java
│                       ├── PayPubViewHolder.java
│                       ├── SearchGoodsViewHolder.java
│                       ├── SearchPayViewHolder.java
│                       ├── SellerOrderAllRefundViewHolder.java
│                       ├── SellerOrderAllViewHolder.java
│                       ├── SellerOrderClosedViewHolder.java
│                       ├── SellerOrderCommentViewHolder.java
│                       ├── SellerOrderFinishViewHolder.java
│                       ├── SellerOrderPayViewHolder.java
│                       ├── SellerOrderReceiveViewHolder.java
│                       ├── SellerOrderRefundViewHolder.java
│                       ├── SellerOrderSendViewHolder.java
│                       ├── SellerShenHeViewHolder.java
│                       ├── SellerXiaJiaViewHolder.java
│                       └── SellerZaiShouViewHolder.java
├── video
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com.yunbao.video
│                   ├── activity
│                   │   ├── AbsVideoCommentActivity.java
│                   │   ├── AbsVideoPlayActivity.java
│                   │   ├── VideoChooseActivity.java
│                   │   ├── VideoChooseClassActivity.java
│                   │   ├── VideoEditActivity.java
│                   │   ├── VideoGoodsAddActivity.java
│                   │   ├── VideoPlayActivity.java
│                   │   ├── VideoPublishActivity.java
│                   │   ├── VideoRecordActivity.java
│                   │   └── VideoReportActivity.java
│                   ├── adapter
│                   │   ├── MusicAdapter.java
│                   │   ├── MusicClassAdapter.java
│                   │   ├── VideoChooseAdapter.java
│                   │   ├── VideoChooseClassAdapter.java
│                   │   ├── VideoCommentAdapter.java
│                   │   ├── VideoGoodsAddAdapter.java
│                   │   ├── VideoPubShareAdapter.java
│                   │   ├── VideoReportAdapter.java
│                   │   ├── VideoScrollAdapter.java
│                   │   └── VideoShareAdapter.java
│                   ├── bean
│                   │   ├── MusicBean.java
│                   │   ├── MusicClassBean.java
│                   │   ├── VideoBean.java
│                   │   ├── VideoChooseBean.java
│                   │   ├── VideoCommentBean.java
│                   │   └── VideoReportBean.java
│                   ├── custom
│                   │   ├── ColorfulProgress.java
│                   │   ├── NumberProgressBar.java
│                   │   ├── RangeSlider.java
│                   │   ├── RangeSliderViewContainer.java
│                   │   ├── RecordProgressView.java
│                   │   ├── SliderViewContainer.java
│                   │   ├── ThumbView.java
│                   │   ├── ThumbnailAdapter.java
│                   │   ├── VideoLoadingBar.java
│                   │   ├── VideoProgressController.java
│                   │   ├── VideoProgressView.java
│                   │   ├── VideoRecordBtnView.java
│                   │   └── ViewTouchProcess.java
│                   ├── dialog
│                   │   ├── VideoGoodsDialogFragment.java
│                   │   ├── VideoInputDialogFragment.java
│                   │   ├── VideoMusicClassDialog.java
│                   │   └── VideoShareDialogFragment.java
│                   ├── event
│                   │   ├── VideoCommentEvent.java
│                   │   ├── VideoDeleteEvent.java
│                   │   ├── VideoLikeEvent.java
│                   │   ├── VideoScrollPageEvent.java
│                   │   └── VideoShareEvent.java
│                   ├── http
│                   │   ├── VideoHttpConsts.java
│                   │   └── VideoHttpUtil.java
│                   ├── interfaces
│                   │   ├── VideoMusicActionListener.java
│                   │   └── VideoScrollDataHelper.java
│                   ├── upload
│                   │   ├── VideoUploadBean.java
│                   │   ├── VideoUploadCallback.java
│                   │   ├── VideoUploadFtpImpl.java
│                   │   ├── VideoUploadQnImpl.java
│                   │   ├── VideoUploadStrategy.java
│                   │   └── VideoUploadTxImpl.java
│                   ├── utils
│                   │   ├── MusicMediaPlayerUtil.java
│                   │   ├── VideoIconUtil.java
│                   │   ├── VideoLocalUtil.java
│                   │   ├── VideoStorge.java
│                   │   └── VideoTextRender.java
│                   └── views
│                       ├── VideoCommentViewHolder.java
│                       ├── VideoEditCutViewHolder.java
│                       ├── VideoEditFilterViewHolder.java
│                       ├── VideoEditMusicViewHolder.java
│                       ├── VideoMusicChildViewHolder.java
│                       ├── VideoMusicCollectViewHolder.java
│                       ├── VideoMusicHotViewHolder.java
│                       ├── VideoMusicSearchViewHolder.java
│                       ├── VideoMusicViewHolder.java
│                       ├── VideoPlayViewHolder.java
│                       ├── VideoPlayWrapViewHolder.java
│                       ├── VideoProcessViewHolder.java
│                       └── VideoScrollViewHolder.java
└── yunbao.jks

