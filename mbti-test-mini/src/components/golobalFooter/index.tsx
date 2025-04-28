import { Component, PropsWithChildren } from "react";
import {Image, View} from "@tarojs/components";
import { AtButton } from "taro-ui";

import "taro-ui/dist/style/components/button.scss"; // 按需引入
import "./index.scss";
import headerBg from "../../assets/headerBg.jpg";

export default class Index extends Component<PropsWithChildren> {
  componentDidMount() {}

  componentWillUnmount() {}

  componentDidShow() {}

  componentDidHide() {}

  render() {
    return (
      <View className="global-footer">
        作者：Hyte, 联系QQ：962230485
      </View>
    );
  }
}
