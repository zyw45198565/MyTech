package com.wd.tech.bean;

public class MyBanner {
        /**
         * imageUrl : https://img.huxiucdn.com/article/cover/201808/31/145637698331.jpg?imageView2/1/w/710/h/400/|imageMog
         * jumpUrl : wd://information/infoId=1
         * rank : 1
         */

        private String imageUrl;
        private String jumpUrl;
        private int rank;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
}
