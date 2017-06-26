package com.Discover.notes;

public class NotesObj {
	public NotesObj(String Title, String Id, String Content, String Time, String Type) {
		// TODO Auto-generated constructor stub
		this.Title = Title;
		this.Id = Id;
		this.Content = Content;
		this.Time = Time;
		this.Type = Type;
	}

	public NotesObj() {

	}

	String Title;
	String Id;
	String Content;
	String Time;
	String Type;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}
}
