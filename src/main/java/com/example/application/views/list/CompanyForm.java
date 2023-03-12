package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class CompanyForm extends FormLayout {
    private Company company;

    TextField name = new TextField("Name");
    TextField country = new TextField("Country");

    Binder<Company> binder = new BeanValidationBinder<>(Company.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public CompanyForm() {
        addClassName("company-form");
        binder.bindInstanceFields(this);
        add(name,
            country,
            createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CompanyForm.DeleteEvent(this, company)));
        close.addClickListener(event -> fireEvent(new CompanyForm.CloseEvent(this)));


        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setCompany(Company company) {
        this.company = company;
        binder.readBean(company);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(company);
            fireEvent(new CompanyForm.SaveEvent(this, company));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class CompanyFormEvent extends ComponentEvent<CompanyForm> {
        private Company company;

        protected CompanyFormEvent(CompanyForm source, Company company) {
            super(source, false);
            this.company = company;
        }

        public Company getCompany() {
            return company;
        }
    }

    public static class SaveEvent extends CompanyForm.CompanyFormEvent {
        SaveEvent(CompanyForm source, Company company) {
            super(source, company);
        }
    }

    public static class DeleteEvent extends CompanyForm.CompanyFormEvent {
        DeleteEvent(CompanyForm source, Company company) {
            super(source, company);
        }

    }

    public static class CloseEvent extends CompanyForm.CompanyFormEvent {
        CloseEvent(CompanyForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
