package reactsandbox

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport


@JSExport("DummyApp")
object App1 extends JSApp {

  @JSExport
  override def main(): Unit = {

    ReactDOM.render(Example1.component1(), dom.document.body)

  }

}

object Data {

  case class Item(name: String)

}

object Example1 {

  var input1Ref = Ref[HTMLInputElement]("input1")
  var input2Ref = Ref[HTMLInputElement]("input2")


  case class Backend($: BackendScope[Unit, Unit]) {

    def focusInput1 = {
      Callback.log("focus empty idea") >> input1Ref.apply($).tryFocus
    }

    def focusInput2 = {
      Callback.log("focus input 2") >> input2Ref.apply($).tryFocus
    }

  }


  val component1 = ReactComponentB[Unit]("Component 1")
    .backend(Backend(_))
    .render { s =>
      <.div(
        ^.className := "component1",
        <.h2("Component 1"),
        <.div(
          <.input(
            ^.`type` := "text",
            ^.ref := input1Ref,
            ^.defaultValue := ""
          ),
          <.button(
            ^.onClick --> s.backend.focusInput1,
            "focus input 1"
          ),
          <.button(
            ^.onClick --> s.backend.focusInput2,
            "focus input 2"
          )
        ),
        <.hr(),
        <.div(
          component2(s.backend)  // pass backend to child component
        )
      )
    }
    .componentDidMount { $ =>
      Callback.log("Component 1 did mount")
    }
    .buildU


  val component2 = ReactComponentB[Backend]("Component 2")
    .renderP { case (s, backend) =>
      <.div(
        ^.className := "component2",
        <.h4("Component 2"),
        <.div(
          <.input(
            ^.`type` := "text",
            ^.ref := input2Ref,    // reference as input2Ref
            ^.defaultValue := ""
          )
        ),
        <.button(
          ^.onClick --> backend.focusInput1,
          "focus input 1"
        ),
        <.button(
          ^.onClick --> backend.focusInput2,
          "focus input 2"
        )
      )
    }
    .componentDidMount { $ =>
      Callback.log("Component 1 did mount")
    }
    .build

}
