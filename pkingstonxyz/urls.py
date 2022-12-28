from django.contrib import admin

from django.urls import path

from staticstuff import views as staticstuffviews
from notes import views as notesviews

urlpatterns = [
    path('', staticstuffviews.index, name='index'),
    path('showcase/', staticstuffviews.showcase, name='showcase'),
    path('about/', staticstuffviews.about, name='about'),
    path('notes/', notesviews.NoteListView.as_view(), name='notes'),
    path('notes/add/', notesviews.AddView.as_view(), name='add_note'),
    path('admin/', admin.site.urls),
]
