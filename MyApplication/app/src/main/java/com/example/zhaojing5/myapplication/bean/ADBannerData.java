package com.example.zhaojing5.myapplication.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ADBannerData  implements Serializable{
    private String bannerType = "";
    private int isAutoPlay = 0;
    private int isShow = 0;
    private int isVoiceShow = 0;
    private String voiceText = "";
    private int duration = 5000;
    private ArrayList<BannerData> bannerList;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public int getIsAutoPlay() {
        return isAutoPlay;
    }

    public void setIsAutoPlay(int isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsVoiceShow() {
        return isVoiceShow;
    }

    public void setIsVoiceShow(int isVoiceShow) {
        this.isVoiceShow = isVoiceShow;
    }

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    public ArrayList<BannerData> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerData> bannerList) {
        this.bannerList = bannerList;
    }

    public static class BannerData implements Serializable{

        private String imageUrl;
        private String title;
        private String actionType;
        private String actionUrl;
        private String actionId = "";

        public String getActionId() {
            return actionId;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        private ArrayList<ButtonData> buttonList;

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getActionUrl() {
            return actionUrl;
        }

        public void setActionUrl(String actionUrl) {
            this.actionUrl = actionUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<ButtonData> getButtonList() {
            return buttonList;
        }

        public void setButtonList(ArrayList<ButtonData> buttonList) {
            this.buttonList = buttonList;
        }

        public static class ButtonData implements  Serializable{
            private String actionType;
            private String actionUrl;
            private String actionId = "";
            private String buttonText;

            public String getActionId() {
                return actionId;
            }

            public void setActionId(String actionId) {
                this.actionId = actionId;
            }

            public String getActionType() {
                return actionType;
            }

            public void setActionType(String actionType) {
                this.actionType = actionType;
            }

            public String getActionUrl() {
                return actionUrl;
            }

            public void setActionUrl(String actionUrl) {
                this.actionUrl = actionUrl;
            }

            public String getButtonText() {
                return buttonText;
            }

            public void setButtonText(String buttonText) {
                this.buttonText = buttonText;
            }
        }
    }
}
