package com.franlina.instituto.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.franlina.instituto.model.Alumno;
import com.franlina.instituto.services.AlumnosServices;

import ch.qos.logback.core.model.Model;

@Controller
@RequestMapping("alumnos")
public class AlumnoController {

    @Autowired
    AlumnosServices alumnosServices;

    @Value("${pagination.size}")
    int sizePage;

    @GetMapping(value = "/list")
    public ModelAndView list(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:list/1/codigo/asc");
        return modelAndView;
    }

    @GetMapping(value = "/list/{numPage}/{fieldSort}/{directionSort}")
    public ModelAndView listPage(Model model,
            @PathVariable("numPage") Integer numPage,
            @PathVariable("fieldSort") String fieldSort,
            @PathVariable("directionSort") String directionSort) {

        Pageable pageable = PageRequest.of(numPage - 1, sizePage,
                directionSort.equals("asc") ? Sort.by(fieldSort).ascending() : Sort.by(fieldSort).descending());

        Page<Alumno> page = alumnosServices.findAll(pageable);

        List<Alumno> alumnos = page.getContent();

        ModelAndView modelAndView = new ModelAndView("alumnos/list");
        modelAndView.addObject("alumnos", alumnos);

        modelAndView.addObject("numPage", numPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("totalElements", page.getTotalElements());

        modelAndView.addObject("fieldSort", fieldSort);
        modelAndView.addObject("directionSort", directionSort.equals("asc") ? "asc" : "desc");

        return modelAndView;
    }

    @RequestMapping(value = { "/new" })
    public ModelAndView nuevo() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alumnos/new");
        return modelAndView;
    }

    @PostMapping(value = "/newAlumno")
    public ModelAndView saveCliente(Alumno alumno, @RequestParam("file") MultipartFile imagen) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        alumno.setImg(imagen.getBytes());
        alumnosServices.insert(alumno);

        modelAndView.setViewName("redirect:edit?codigo=" + alumno.getCodigo());
        return modelAndView;
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit(
            @RequestParam(name = "codigo", required = true) int codigo) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("alumno", alumnosServices.findById(codigo));
        modelAndView.setViewName("alumnos/edit");
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView editCliente(Alumno alumno, @RequestParam("file") MultipartFile imagen) throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        alumno.setImg(imagen.getBytes());
        alumnosServices.update(alumno);

        modelAndView.setViewName("redirect:edit?codigo=" + alumno.getCodigo());
        return modelAndView;
    }

    @RequestMapping(value = "/borrarAlumno")
    public ModelAndView delete(
            @RequestParam(name = "codigo", required = true) int codigo) {

        alumnosServices.delete(codigo);
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:list/1/codigo/asc");
        return modelAndView;
    }

}
