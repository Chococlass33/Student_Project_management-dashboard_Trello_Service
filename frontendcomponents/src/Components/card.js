import React from "react";
import { Stylesheet, View } from "react";

export default function Card(props) {
  return (
    <View style={StyleSheet.card}>
      <View style={StyleSheet.cardContent}>{props.children}</View>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    borderRadius: 6,
    elevation: 3,
    backgroundColor: "#fff",
    shadowOffset: { width: 1, height: 1 },
    shadowsColor: "#333",
    shadowOpacity: 0.3,
    shadowRadius: 2,
    marginHorizontal: 4,
    marginVertical: 6,
  },
  cardcontent: {
    marginHorizontal: 18,
    marginVertical: 10,
  },
});
