package thesimpleton.events;

import com.megacrit.cardcrawl.events.AbstractImageEvent;

public abstract class CustomSimpletonEvent extends AbstractImageEvent {
  public CustomSimpletonEvent(String title, String body, String imgUrl) {
    super(title, body, imgUrl);
  }
}
