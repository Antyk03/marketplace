package antyk03.marketplaceserver.modello.dto;

import jakarta.validation.Valid;

public class RegistrazioneUtenteDTO {
    @Valid
    private UtenteDTO utenteDTO;

    @Valid
    private DatiUtenteDTO datiUtenteDTO;

    // getters e setters
    public UtenteDTO getUtenteDTO() { return utenteDTO; }
    public void setUtenteDTO(UtenteDTO utenteDTO) { this.utenteDTO = utenteDTO; }

    public DatiUtenteDTO getDatiUtenteDTO() { return datiUtenteDTO; }
    public void setDatiUtenteDTO(DatiUtenteDTO datiUtenteDTO) { this.datiUtenteDTO = datiUtenteDTO; }
}
