import {View, Image} from "@tarojs/components";
import {AtButton, AtRadio} from "taro-ui";
import {useEffect, useState} from "react";
import GlobalFooter from "../../components/golobalFooter";
import "./index.scss";
import questions from "../../data/questions.json";
import Taro from "@tarojs/taro";



export default () => {

  // 当前题目序号
  const [current, setCurrent] = useState<number>(1)

  // 当前题目
  const [currentQuestion, setCurrentQuestion] = useState(questions[current -1])
  const questionOptions = currentQuestion.options.map((option) => {
    return {label: `${option.key}. ${option.value}`, value: option.key};
  })
  // 当前题目答案
  const [currentAnswer, setCurrentAnswer] = useState('')

  // 回答题目列表
  const [answerList] = useState<string[]>([])

  // 序号变化时，切换题目和回答选项
  useEffect(() => {
    setCurrentQuestion(questions[current -1])
    setCurrentAnswer(answerList[current -1])
  }, [current])

  return (
    <View className="doQuestionPage">
      <View className="at-article__h2 title">
        {current}、{currentQuestion.title}
      </View>
      <AtRadio
        options={questionOptions}
        value={currentAnswer}
        onClick={(value) => {
          setCurrentAnswer(value)
          answerList[current - 1] = value
        }}
      />
      {current == questions.length && (<AtButton
        type="primary"
        circle
        className="controlBtn"
        onClick={() => {
          // 传递答案
          Taro.setStorageSync("answerList", answerList)
          // 跳转到答案页面
          Taro.navigateTo({
            url: "/pages/result/index",
          });
        }}
      >
        结果
      </AtButton>)}
      {current  < questions.length && (<AtButton
        type="primary"
        circle
        className="controlBtn"
        onClick={() => {
          setCurrent(current + 1)
        }}
      >
        下一题
      </AtButton>)}
      {current > 1 && (
        <AtButton
          circle
          className="controlBtn"
          onClick={() => {
            setCurrent(current - 1)
          }}
        >
          上一题
        </AtButton>)}
      <GlobalFooter />
    </View>
  );
};
