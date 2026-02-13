/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.controller;

import com.tienda.domain.Categoria;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/categoria")

/**
 *
 * @author Elioth Artavia
 */
public class CategoriaController {
    
     @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listado")
    public String inicio(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());
        return "/categoria/listado";
    }
    

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/guardar")
    public String guardar(@Valid Categoria categoria, @RequestParam MultipartFile imagenFile, RedirectAttributes redirectAttributes) {
        categoriaService.save(categoria, imagenFile);
            redirectAttributes.addFlashAttribute("codigoOk", messageSource.getMessage("mensaje.actualizado", null, locale.getDefault()));
        return "redirect:/categoria/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCategoria, RedirectAttributes redirectAttributes) {
        String titulo="codigoOk";
        String detalle="mensaje.eliminado";
        try {
            categoriaService.delete(idCategoria);
        } catch (IllegalArgumentException e) {
        // Captura la excepción de argumento inválido para el mensaje de "no existe"
            titulo="error";
            detalle="categoria.error01";
        } catch (IllegalStateException e) {
        // Captura la excepción de estado ilegal para el mensaje de "datos asociados"
            titulo="error";
            detalle="categoria.error02";
        } catch (Exception e) {
        // Captura cualquier otra excepción inesperada
            titulo="error";
            detalle="categoria.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/categoria/listado";
    }

    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable("idCategoria") Integer idCategoria, Model model, RedirectAttributes redirectAttributes) {
        Optional<Categoria> categoriaOpt = categoriaService.getCategoria(idCategoria);
        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("categoria.error01", null, Locale.getDefault()));
            return "redirect:/categoria/listado";
        }
        model.addAttribute("categoria", categoriaOpt.get());
        return "/categoria/modifica";
    }
}
