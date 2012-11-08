package com.collablist.web.snippet

import net.liftweb.util.Helpers._
import com.collablist.web.model.ListEntry
import net.liftweb.common.{Empty, Logger, Full}
import net.liftweb.http.{StatefulSnippet, SHtml, S}
import xml.{Text, NodeSeq, Node}
import java.util.Date

class ListEdit extends StatefulSnippet  {
  val logger = Logger(classOf[ListEdit])

  def dispatch: DispatchIt   = {
    case "addEntry" => addEntry _
    case "showList" => list _
  }

  def list(seq : NodeSeq ):NodeSeq = {
    ListEntry.findAll().flatMap(ent =>
    bind("le", chooseTemplate("list", "entry", seq),
      "text" -> Text(ent.text.is),
      "delete" -> { SHtml.link("/", () => {ent.delete_!; S.notice("Entry deleted!")}, Text("X"))}))
  }

  def prevalidate(entry: ListEntry): Boolean ={
    val requiredFields = List((entry.text.is, "text"))

    val missing = requiredFields.flatMap { case (field,label) =>
      if (field.trim.length == 0) {
        Some(label)
      } else {
        None
      }
    }

    if (! missing.isEmpty) {
      missing.foreach { label =>
        logger.error("Entry add attempted without " + label)
        S.error("You must provide " + label)
      }
    }
    missing.isEmpty
  }

  def add (t : String) = {
    val e = ListEntry.create.text(t);
    // Pre-validate the unparsed values
    if (prevalidate(e)){
    e.validate match {
      case Nil => {
        e.save()
        S.notice("Entry added!")
        this.unregisterThisSnippet() // dpp: remove the statefullness of this snippet
        S.redirectTo("/")
      }
      case x => S.error(x)
    }
    }
  }

  def addEntry(in : NodeSeq )= {

    bind("e", in,
      "text" -%> SHtml.text("", add _ ))
  }

}

