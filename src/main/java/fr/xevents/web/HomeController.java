/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.xevents.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(Model model) {
        List<Event> events = createMockedEvents();
        model.addAttribute("events", events);
        return "home";
    }

    private List<Event> createMockedEvents() {
        List<Event> events = new ArrayList<Event>();
        events.add(new Event(
                System.currentTimeMillis(),
                "bguerout",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer rutrum suscipit sem, eu blandit urna porta eu. Phasellus fringilla eleifend accumsan."));
        events.add(new Event(
                System.currentTimeMillis(),
                "bguerout",
                "Nullam posuere elit vel justo facilisis volutpat. Vestibulum id nisi at erat semper venenatis. Duis at ultrices velit. Nam mollis, ante id cursus accumsan"));
        events.add(new Event(
                System.currentTimeMillis(),
                "bguerout",
                "Nullam ut blandit mauris. Mauris vitae porttitor tellus. Maecenas rutrum nunc bibendum arcu convallis tincidunt. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend blandit ultrices"));
        events.add(new Event(
                System.currentTimeMillis(),
                "bguerout",
                "Proin consectetur justo ut nunc aliquet commodo. Nam suscipit risus sit amet mi pulvinar mollis. Praesent quis accumsan nisl. Suspendisse accumsan posuere tempor. Sed eget ligula et diam sollicitudin laoreet ut at quam. In tristique accumsan tempus. Nulla porta purus eu orci mollis tristique. Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
        events.add(new Event(
                System.currentTimeMillis(),
                "bguerout",
                "Mauris posuere molestie ante vel eleifend. Cras erat nulla, malesuada nec laoreet sit amet, lacinia et ante. Morbi dictum lacus ac lectus posuere ut egestas lacus euismod. Aliquam erat volutpat. Morbi laoreet, lectus eu vehicula vehicula, nisl nisi sollicitudin erat, ut iaculis est velit sed leo. Praesent ornare rhoncus mi, ac blandit ante commodo ac"));
        return events;
    }

}
