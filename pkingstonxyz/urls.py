from django.contrib import admin

from django.urls import path

from staticstuff import views as staticstuffviews
from notes import views as notesviews
from blog import views as blogviews

urlpatterns = [
    path('', staticstuffviews.index, name='index'),
    path('blog/', blogviews.PostListView.as_view(), name='blog'),
    path('showcase/', staticstuffviews.showcase, name='showcase'),
    path('about/', staticstuffviews.about, name='about'),
    path('notes/', notesviews.NoteListView.as_view(), name='notes'),
    path('notes/add/', notesviews.AddView.as_view(), name='add_note'),
    path('admin/', admin.site.urls),
]
