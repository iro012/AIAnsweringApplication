/**
 * 获取最佳题目评分结果
 * @param answerList
 * @param questions
 * @param question_result
 */
export function getBestQuestionResult(answerList, questions, question_result) {
  // 初始化一个对象，用于存储每个选项的计数
  const optionCount = {};

  // 遍历答案列表和题目列表
  for (let i = 0; i < answerList.length; i++) {
    const answer = answerList[i];
    const question = questions[i];

    // 遍历题目中的选项
    for (const option of question.options) {
      // 如果答案和选项的key匹配
      if (option.key === answer) {
        const result = option.result;

        // 如果result属性不在optionCount中，初始化为0
        if (!optionCount[result]) {
          optionCount[result] = 0;
        }

        // 将对应的result计数加1
        optionCount[result]++;
      }
    }
  }

  // 初始化最高分数和对应的评分结果
  let maxScore = 0;
  let maxScoreResult = question_result[0];

  // 遍历评分结果列表
  for (const result of question_result) {
    // 获取当前评分结果的计数
    const score = result.resultProp.reduce((count, prop) => {
      return count + (optionCount[prop] || 0)
    }, 0);

    // 如果当前评分结果的计数大于最高分数，更新最高分数和对应的评分结果
    if (score > maxScore) {
      maxScore = score;
      maxScoreResult = result;
    }
  }

  // 返回最高分数对应的评分结果
  return maxScoreResult;
}


