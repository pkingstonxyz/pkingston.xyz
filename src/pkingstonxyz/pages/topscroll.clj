(ns pkingstonxyz.pages.topscroll

  (:require [pkingstonxyz.ankistats :as ankistats]
            [pkingstonxyz.db :as db]
            [hiccup2.core :as h]))

(def colors (cycle ["var(--king)" "var(--queen)" "var(--rook)" "var(--bishop)" "var(--knight)" "var(--pawn)"]))

(for [item (sort-by second > (ankistats/get-study-data))]
  [:div {:style (str "color: " (nth colors (second item)) ";")}
   [:p
    (first item)]])

(defn topelement []
  (let [ankidata (sort-by second > (ankistats/get-study-data))]
    (str 
      (h/html
        [:div#topMenu
         [:div.menu-container
          [:div.menu-items
           [:div [:p "Today's Flashcard Reviews:"]]
           (for [item ankidata]
             [:div {:style (str "color: " (nth colors (second item)) ";")}
              [:p
               (first item)
               ": "
               (second item)]])
           [:div [:p "|"]]
           [:div [:a {:href "/rss"} [:p "Subscribe to my " 
                                     [:span {:style "color: var(--bishop);"}"RSS"]
                                     " feed"]]]
           [:div [:p "|"]]
           [:div [:p "Currently Reading: " (db/get-reading)]]
           [:div [:p "|"]]

           ;Duplicate starts here
           [:div [:p "Today's Flashcard Reviews:"]]
           (for [item ankidata]
             [:div {:style (str "color: " (nth colors (second item)) ";")}
              [:p
               (first item)
               ": "
               (second item)]])
           [:div [:p "|"]]
           [:div [:a {:href "/rss"} [:p "Subscribe to my " 
                                     [:span {:style "color: var(--bishop);"}"RSS"]
                                     " feed"]]]
           [:div [:p "|"]]
           [:div [:p "Currently Reading: " (db/get-reading)]]
           [:div [:p "|"]]
           ]]
         [:button#toggleHideBtn "Hide ▲"]]
        [:div#showArrow "▼"]
        [:script (h/raw "const topMenu = document.getElementById('topMenu');
                        const toggleBtn = document.getElementById('toggleHideBtn');
                        const showArrow = document.getElementById('showArrow');

                        toggleBtn.addEventListener('click', () => {
                                                                   topMenu.classList.add('hidden');
                                                                   toggleBtn.style.display = 'none';

                                                                   setTimeout(() => {
                                                                                     showArrow.classList.remove('hidden');
                                                                                     showArrow.classList.add('visible');
                                                                                     }, 300);
                                                                   });

                        showArrow.addEventListener('click', () => {
                                                                   topMenu.classList.remove('hidden');
                                                                   showArrow.classList.remove('visible');
                                                                   showArrow.classList.add('hidden');

                                                                   setTimeout(() => {
                                                                                     toggleBtn.style.display = 'inline';
                                                                                     }, 300);
                                                                   });")]
        ))))

; "<script>const topMenu = document.getElementById('topMenu');\n  const toggleBtn = document.getElementById('toggleHideBtn');\n  const showArrow = document.getElementById('showArrow');\n\n  toggleBtn.addEventListener('click', () => {\n    topMenu.classList.add('hidden');\n    toggleBtn.style.display = 'none';\n\n    setTimeout(() => {\n      showArrow.classList.remove('hidden');\n      showArrow.classList.add('visible');\n    }, 300);\n  });\n\n  showArrow.addEventListener('click', () => {\n    topMenu.classList.remove('hidden');\n    showArrow.classList.remove('visible');\n    showArrow.classList.add('hidden');\n\n    setTimeout(() => {\n      toggleBtn.style.display = 'inline';\n    }, 300);\n  });</script><div id=\"topMenu\"><div class=\"menu-items\"><div>foo</div><div>bar</div><div>baz</div></div><button id=\"toggleHideBtn\"></button></div><div id=\"showArrow\">▼</div>"
; "<div id=\"topmenu\"><div class=\"topmenu-items\"></div><button id=\"toggle\"></button></div><div id=\"showArrow\">▼</div>"
