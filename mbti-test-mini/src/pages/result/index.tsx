import {Image, View} from "@tarojs/components";
import GlobalFooter from "../../components/golobalFooter";
import "./index.scss";
import {AtButton} from "taro-ui";
import Taro from "@tarojs/taro";
import headerBg from "../../assets/headerBg.jpg";
import questions from "../../data/questions.json";
import questionResult from "../../data/question_results.json";
import {getBestQuestionResult} from "../../utils/bizUtils";



export default () => {

  // 获取答案
  const answerList = Taro.getStorageSync("answerList")
  if (!answerList || answerList.length < 1) {
    Taro.showToast({
      title: "答案为空",
      icon: "error",
      duration: 3000,
    });
  }
  const result = getBestQuestionResult(answerList, questions, questionResult)

  return (
    <View className="resultPage">
      <View className="at-article__h1 title">{ result.resultName }</View>
      <View className="at-article__h2 subTitle">
        { result.resultDesc }
      </View>
      <AtButton
        type="primary"
        circle
        className="enterBtn"
        onClick={() => {
          Taro.navigateTo({
            url: "/pages/index/index",
          });
        }}
      >
        返回首页
      </AtButton>
      <Image className="headerBg" src={headerBg} />
      <GlobalFooter />
    </View>
  );
};
